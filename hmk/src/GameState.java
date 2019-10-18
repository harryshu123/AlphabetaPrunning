import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
    private int size;            // The number of stones
    private boolean[] stones;    // Game state: true for available stones, false for taken ones
    private int lastMove;        // The last move

    /**
     * Class constructor specifying the number of stones.
     */
    public GameState(int size) {

        this.size = size;

        //  For convenience, we use 1-based index, and set 0 to be unavailable
        this.stones = new boolean[this.size + 1];
        this.stones[0] = false;

        // Set default state of stones to available
        for (int i = 1; i <= this.size; ++i) {
            this.stones[i] = true;
        }

        // Set the last move be -1
        this.lastMove = -1;
    }

    //get the stones
    public boolean[] getStones() {
    	return stones;
    }
    
    /**
     * Copy constructor
     */
    public GameState(GameState other) {
        this.size = other.size;
        this.stones = Arrays.copyOf(other.stones, other.stones.length);
        this.lastMove = other.lastMove;
    }


    /**
     * This method is used to compute a list of legal moves
     *
     * @return This is the list of state's moves
     */
    public List<Integer> getMoves() {
    	List<Integer> legalMoves = new ArrayList<>();
        if (lastMove == -1) {
            for (int i = 1; i < (double) size / 2; i++)
                if (this.stones[i]&&(i % 2) != 0)
                    legalMoves.add(i);
        } else {
            for (int i = 1; i <= size; i++) {
                if (this.stones[i] && (i % lastMove == 0 || lastMove % i == 0))
                	legalMoves.add(i);
            }
        }
        return legalMoves;
    }


    /**
     * This method is used to generate a list of successors
     * using the getMoves() method
     *
     * @return This is the list of state's successors
     */
    public List<GameState> getSuccessors() {
        return this.getMoves().stream().map(move -> {
            var state = new GameState(this);
            state.removeStone(move);
            return state;
        }).collect(Collectors.toList());
    }


    /**
     * This method is used to evaluate a game state based on
     * the given heuristic function
     *
     * @return int This is the static score of given state
     * 
     * if it is Player 1 (MAX)’s turn:
 If stone 1 is not taken yet, return a value of 0 (because the current state is a
relatively neutral one for both players)
 If the last move was 1, count the number of the possible successors (i.e., legal
moves). If the count is odd, return 0.5; otherwise, return -0.5.
 If last move is a prime, count the multiples of that prime in all possible
successors. If the count is odd, return 0.7; otherwise, return -0.7.
 If the last move is a composite number (i.e., not prime), find the largest prime
that can divide last move, count the multiples of that prime, including the
prime number itself if it hasn’t already been taken, in all the possible
successors. If the count is odd, return 0.6; otherwise, return -0.6. 
     */
    public double evaluate() {
    	if(stones[1]) {
    		return 0;
    	}
    	List moves = this.getMoves();
    	int count = 0;
    	Double result;
    	
    	//determine which player right now
    	for(int i = 1; i <= this.size; i++) {
    		if(!this.stones[i]) {
    			count++;
    		}
    	}
    	boolean player;
    	if(count % 2 == 0) {
    		player = true; // player == max
    	}else {
    		player = false; // player == min
    	}
    	
    	
    	
    	//end states
    	if(this.getMoves().size() == 0 && player) {
    		return -1.0;
    	}else if(this.getMoves().size() == 0 && !player){
    		return 1.0;
    	}
    	
    	//if it is max's turn
    	if(player && moves.size() != 0) {
    		if(stones[1]) {
    			return 0.0;
    		}else if(lastMove == 1) {
    			List successors = this.getMoves();
    			if(successors.size() % 2 == 0) {
    				return -0.5;
    			}else {
    				return 0.5;
    			}
    		}else if(Helper.isPrime(lastMove)) {
    			int countp = 0;
    			List<Integer> successors = this.getMoves();
    			//not including itself
    			for(int n: successors) {
    				if(n % lastMove == 0 && lastMove != n) {
    					countp++;
    				}
    			}
    			if(countp % 2 ==0) {
    				return -0.7;
    			}else {
    				return 0.7;
    			}
    		}else if(!Helper.isPrime(lastMove)) {
    			int largestP = Helper.getLargestPrimeFactor(lastMove);
    			int countnp = 0;
    			List<Integer> successors = this.getMoves();
    			for(int n: successors) {
    				if(n % largestP == 0) {
    					countnp++;
    				}
    			}
    			if(countnp % 2 == 0) {
    				return -0.6;
    			}else {
    				return 0.6;
    			}
    		}
    	//min's turn
    	}else {
    		if(stones[1] == true) {
    			return 0.0;
    		}else if(lastMove == 1) {
    			List successors = this.getMoves();
    			if(successors.size() % 2 == 0) {
    				return 0.5;
    			}else {
    				return -0.5;
    			}
    		}else if(Helper.isPrime(lastMove)) {
    			int countp = 0;
    			List<Integer> successors = this.getMoves();
    			//not including itself
    			for(int n: successors) {
    				if(n % lastMove == 0 && lastMove != n) {
    					countp++;
    				}
    			}
    			if(countp % 2 ==0) {
    				return 0.7;
    			}else {
    				return -0.7;
    			}
    		}else if(!Helper.isPrime(lastMove)) {
    			int largestP = Helper.getLargestPrimeFactor(lastMove);
    			int countnp = 0;
    			List<Integer> successors = this.getMoves();
    			for(int n: successors) {
    				if(n % largestP == 0) {
    					countnp++;
    				}
    			}
    			if(countnp % 2 == 0) {
    				return 0.6;
    			}else {
    				return -0.6;
    			}
    		}
    	}
    	
    	System.out.println("not going right! evaluate()");
        return 0.0;
    }


    /**
     * This method is used to take a stone out
     *
     * @param idx Index of the taken stone
     */
    public void removeStone(int idx) {
        this.stones[idx] = false;
        this.lastMove = idx;
    }

    /**
     * These are get/set methods for a stone
     *
     * @param idx Index of the taken stone
     */
    public void setStone(int idx) {
        this.stones[idx] = true;
    }

    public boolean getStone(int idx) {
        return this.stones[idx];
    }

    /**
     * These are get/set methods for lastMove variable
     *
     * @param move Index of the taken stone
     */
    public void setLastMove(int move) {
        this.lastMove = move;
    }

    public int getLastMove() {
        return this.lastMove;
    }

    /**
     * This is get method for game size
     *
     * @return int the number of stones
     */
    public int getSize() {
        return this.size;
    }

}	

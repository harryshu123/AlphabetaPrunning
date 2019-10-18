import java.util.ArrayList;



public class AlphaBetaPruning {

	int Move ;
	double Value = 0.0;
	int NONVisit = 0;
	int NONEva = 0;
	int maxDepth = 0;
	int minNumber = 0;
	int Depth = 0;
	double CountF = 0;
	double BestMove;
	

	
    public AlphaBetaPruning() {
    	
    }

    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
       
    	System.out.println("Move: " + Move);
    	
    	System.out.printf("Value: %.1f %n", Value);
    	
    	System.out.println("Number of Nodes Visited: " + NONVisit);

    	System.out.println("Number of Nodes Evaluated: " + NONEva);

    	System.out.println("Max Depth Reached: " + maxDepth);

    	System.out.printf("Avg Effective Branching Factor: %.1f %n", CountF/(NONVisit - NONEva));

    	

    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth) {
    		Depth = depth;
    		
    		int trueNum = 0;
    		boolean stonesb[] = state.getStones();
    		for(int i = 1; i < state.getStones().length; i++) {
    			if(!stonesb[i]) {
    				trueNum++;
    			}
    		}
    		if(trueNum % 2  == 0) {
    			
        		Value = alphabeta(state,state,depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
        		
    		}else {
        		Value = alphabeta(state,state, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
        		

    		}
    }

    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the current game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @param maxPlayer True if player is Max Player; Otherwise, false
     * @return int This is the number indicating score of the best next move
     */
   
    private double alphabeta(GameState state,GameState parent, int depth, double alpha, double beta, boolean maxPlayer) {
    	
    	NONVisit++;
    	if(depth == 0 || state.getSuccessors().size() == 0) {
    		
    		if ((Depth - depth) > maxDepth) {
    			//update maxdepth
    			//System.out.println(Depth + " " + depth);
    			maxDepth = Depth - depth;
    			
    		}
                
    	
    		NONEva++;
    		int last = state.getLastMove();
    		//Value = state.evaluate();
    		
	    	return state.evaluate();
	    	
    	}
    	//update max depth
    	//maxDepth++;
    	

    	
    	// for Max
    	if(maxPlayer) {
    		//System.out.println("evalueate max= " + state.getSuccessors().size() + " depth " + depth);
       	 	double val = Double.NEGATIVE_INFINITY;
       	 	double bestv =  Double.NEGATIVE_INFINITY;
       	 	for(GameState s: state.getSuccessors()) {
        	CountF++;
        	val = Double.max(val, alphabeta(s,state,depth - 1, alpha, beta,false));
        	
        	if(bestv < val && depth == Depth) {
        		//System.out.println("---------------------max");

                bestv = val; 
                Move = s.getLastMove();
            }
        	if(val >= beta) {
        		return val;
        	}
        	alpha = Double.max(alpha,val);
        	
        }
    		
        return val;
      //for Min
    	}else {
    		//System.out.println("evaluate min= " + state.getSuccessors().size() + " depth " + depth);
    		 double val = Double.POSITIVE_INFINITY;
    		 double bestv = Double.POSITIVE_INFINITY;
    		 for(GameState s: state.getSuccessors()) {
    			    CountF++;
    	        	val = Double.min(val, alphabeta(s,state,depth - 1, alpha, beta,true));
    	        	if(bestv > val && depth == Depth) {
    	        		
    	        		//System.out.println("---------------------min");
    	        		
    	                bestv = val; 
    	                Move = s.getLastMove();
    	        	}
    	        	if(val <= alpha) {   	        		
    	        		return val;
    	        	}
    	        	beta = Double.min(beta,val);
    	        	
    	        }
     		
    		 return val;
    	}
    }
 
}

public class Helper {
	
	/** 
    * Class constructor.
    */
	private Helper () {}

	/**
	* This method is used to check if a number is prime or not
	* @param x A positive integer number
	* @return boolean True if x is prime; Otherwise, false
	*/
	public static boolean isPrime(int x) {
		if(x < 3) {
			return x > 1;
		}else if(x % 2 == 0 || x % 3 == 0){
			return false;
		}
		int i = 5;
		//6k +- 1 <= Sqrt(x)
		while(i*i < x) {
			if((x % i == 0) || (x % (i+2) == 0)) {
				return false;
			}
			i = i+6;
		}

		return true;
	}

	/**
	* This method is used to get the largest prime factor 
	* @param x A positive integer number
	* @return int The largest prime factor of x
	*/
	public static int getLargestPrimeFactor(int x) {
		if(x < 0) {
			System.out.println("invalid x number!");
			return -1;
		}else {
			int largest = 2;
			while(x > largest) {
				if(x % largest == 0) {
					x /= largest;
					largest = 2;
				}else {
					largest++;
				}
			}
			return largest;
		}
    }
}
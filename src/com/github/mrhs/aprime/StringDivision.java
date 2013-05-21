import java.math.BigDecimal;

/** Division of string values
*
* @author Gunther Cox
* @version 1.0
*/


// MISCALANEOUS METHODS THAT I HAVE COLLECTED
public class StringDivision {
  
	// MAIN METHOD FOR TESTING
	/*public static void main(String[] args) {
	  
		System.out.println(stringDivision("100000", "314159"));
		
		System.out.println(divide("100000", "314159"));
		System.out.println(stringIsInteger("44444.43"));
		
	}*/

	/*
	 * Dear sir or madam,
	 * please ignore this first method as it is an attempt to
	 * utilize fortier division using strings. I have plans to continue
	 * this method in the future when I have the time to under go a
	 * complete study of the concepts behind fortier division. For now, 
	 * please use the smaller methods using the java big decimal library
	 * that I have included at the end of this document.
	 * 
	 * http://en.wikipedia.org/wiki/Fourier_division
	 * 
	 */
	public static String stringDivision(String str1, String str2) {
		
		// STORES A LIST OF THE NUMBERS AS PAIRS OF TWO INTEGERS
		int[] c = new int[str1.length()];
		int[] a = new int[str2.length()];
		
		// STORES DIVISED PAIRS OF INTEGERS
		int[] b = new int[str1.length()];
		int[] r = new int[str1.length()];
		
		// GENERATED DIVISED PAIRS OF INTEGERS
		for (int i = 0; i < str1.length(); i+=2) {
			
			// STORES SETS OF TWO DIGIT INTEGERS
			c[i] = Integer.parseInt(str1.substring(i, i + 2));
			a[i] = Integer.parseInt(str2.substring(i, i + 2));

		}

		b[0] = concatIntegers(c[0], c[1]) / a[0];
		//System.out.println(b[0]);
		
		b[1] = (concatIntegers(r[0], c[2]) - b[0] * a[1]) / a[0];

		for (int j = 1; j < b.length - 1; j++) {
			//System.out.println(b[j]);
			
			// STOPPED HERE
			
		}
		
		// RETURN THE LAST VALUE IN THE b ARRAY
		return concatIntArray(b);
	}

	
	// CONCATINATE TWO INTEGERS
	public static int concatIntegers(int a, int b) {
		String c = String.valueOf(a) + String.valueOf(b);
		return Integer.parseInt(c);
	}
	
	// CONCATINATE ARRAY OF INTEGERS INTO A STRING
	public static String concatIntArray(int[] a) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			s.append(String.valueOf(a[i]));
		}
		return s.toString();
	}
	
	// ###############################################
	// HEY FOLKS! THIS IS WHERE THE CODE REALLY STARTS
	// ###############################################
	
	/** String division using BigDecimal
	 * 
	 * @param a			Any numeric string
	 * @param b         Any numeric string
	 * @return			Returns a divided by b
	 */
	public static BigDecimal divide(String a, String b) {
		
		// CONVERT STRINGS TO BIG DECIMALS
		BigDecimal num1 = new BigDecimal(a);
		BigDecimal num2 = new BigDecimal(b);
        
		// RETURN BIG DECIMAL
		return num2.divide(num1);
	}
	
	/** Converts a BigDecimal to a string
	 * 
	 * @param b			A BigDecimal value
	 * @return			Returns a string value of the big decimal
	 */
	public static String bigDecimalToString(BigDecimal b) {
		return b.toString();
	}
	
	/** Checks a string to determine if it in an integer
	 * 
	 * @param x			Any numeric string
	 * @return			Returns true if the string is an integer
	 */
	public static boolean stringIsInteger(String x) {
		if (x.contains(".")) {
			return false;
		}
		return true;
	}
	
}

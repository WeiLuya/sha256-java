package hash;

public class Pbkdf {
	Sha sha = new Sha();

	String dk = new String();
	
	public static void main(String args[]) {
		Pbkdf p = new Pbkdf();
		System.out.println(p.stretch("user1234", 32455, 4096, 2));
	}
	
	public String stretch(String password, int salt, int iterations, int shortNumber) {
		if(iterations <= 0) {
			iterations = 1;
		}
		
		if(shortNumber > 0) {
			byte[] key = new byte[32];
			
			for(int i = 0; i < 4; i++) {
				key[i] = (byte) (salt >>> (i * 8));
				key[i + 4] = (byte) (salt >>> (i * 8));
				key[i + 8] = (byte) (salt >>> (i * 8));
				key[i + 12] = (byte) (salt >>> (i * 8));
				key[i + 16] = (byte) (shortNumber >>> (i * 8));
				key[i + 20] = (byte) (shortNumber >>> (i * 8));
				key[i + 24] = (byte) (shortNumber >>> (i * 8));
				key[i + 28] = (byte) (shortNumber >>> (i * 8));
			}
			
			dk = dk + f(password, key, iterations);
			return stretch(password, salt, iterations, shortNumber - 1);
		} else {
			return dk;
		}
	}
	
	public String f(String password, byte[] key, int len) {

		byte[][] bob = new byte[len][32];
		String temp = password + new String(key);
		
		byte[] hash = sha.shaString(temp);
		for(int i = 0; i < 32; i++) {
			bob[0][i] = (byte) (hash[i]);
		}
		
		for(int i = 1; i < len; i++) {
			
			temp = password + new String(bob[i - 1]);
			
			hash = sha.shaString(temp);
			
			for(int j = 0; j < 32; j++) {
				bob[i][j] = (byte) (hash[j]);
			}
					
		}
		
		byte[] output = new byte[32];
		
		for(int i = 0; i < 32; i++) {
			output[i] = bob[0][i];
		}
		
		for(int i = 0; i < bob[0].length; i++) {
			for(int j = 1; j < bob.length; j++) {
				output[i] = (byte) (bob[j][i] ^ bob[j - 1][i]);
			}
		}
		
		final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[output.length * 2];
	    for (int j = 0; j < output.length; j++) {
	        int v = output[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    
	    String out = new String();
	    for(int i = 0; i < hexChars.length; i++) {
	    	out = out + hexChars[i];
	    }
	    
	    return out;
	}
}

package sha;

import java.util.Random;

public class Pbkdf {
	Sha sha = new Sha();

	String dk = new String();
	
	public static void main(String args[]) {
		Pbkdf p = new Pbkdf();
		System.out.println(p.stretch("user1234", 1, 1024, 3));
	}
	
	public String stretch(String password, int salt, int c, int shortNumber) {
		
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
			dk = dk + f(password.getBytes(), key, c);
			
			return stretch(password, salt, c, shortNumber - 1);
		} else {
			return dk;
		}
	}
	
	public String f(byte[] password, byte[] key, int len) {
		

		byte[] bob = new byte[32];
		byte[] hash = sha.shaByteArray(password);
		for(int i = 0; i < 32; i++) {
			bob[i] = (byte) (hash[i] ^ key[i]);
		}
		
		for(int i = 1; i < len; i++) {
			hash = sha.shaByteArray(bob);
			for(int j = 0; j < 32; j++) {
				bob[j] = (byte) (hash[j] ^ bob[j]);
			}
			
		}
		
		final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bob.length * 2];
	    for (int j = 0; j < bob.length; j++) {
	        int v = bob[j] & 0xFF;
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

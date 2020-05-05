package sha;

public class Sha {

	
	  public static void main(String args[]) {
	  
	  Sha s = new Sha();
	  
	  byte[] output = s.shaString("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq");
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
	  
	  System.out.println(out);
	  
	  
	  }
	 

	/* constants */
	final private int[] k = { 0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
			0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174, 0xe49b69c1,
			0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da, 0x983e5152, 0xa831c66d,
			0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967, 0x27b70a85, 0x2e1b2138, 0x4d2c6dfc,
			0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85, 0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3,
			0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070, 0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3,
			0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3, 0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb,
			0xbef9a3f7, 0xc67178f2 };

	/* default initial values 
	private int h0 = 0x6a09e667;
	private int h1 = 0xbb67ae85;
	private int h2 = 0x3c6ef372;
	private int h3 = 0xa54ff53a;
	private int h4 = 0x510e527f;
	private int h5 = 0x9b05688c;
	private int h6 = 0x1f83d9ab;
	private int h7 = 0x5be0cd19;
	*/

	public byte[] shaByteArray(byte[] a) {

		byte[][] newArray = pad(a);

		int[] hash = hash(newArray);

		byte[] out = new byte[32];

		for (int i = 0; i < 8; i++) {
			out[(4 * i) + 3] = (byte) (hash[i]);
			out[(4 * i) + 2] = (byte) (hash[i] >>> 8);
			out[(4 * i) + 1] = (byte) (hash[i] >>> 16);
			out[(4 * i) + 0] = (byte) (hash[i] >>> 24);
		}

		return out;
	}

	public byte[] shaString(String b) {
		byte[] a = b.getBytes();

		byte[][] newArray = pad(a);
				
	/*	for(int i = 0; i < newArray.length; i++) {
			for(int j = 0; j < newArray[i].length; j++) {
				System.out.print(newArray[i][j] + ",");
			}
			System.out.print("\n");
		}*/

		int[] hash = hash(newArray);

		byte[] out = new byte[32];

		for (int i = 0; i < 8; i++) {
			out[(4 * i) + 3] = (byte) (hash[i]);
			out[(4 * i) + 2] = (byte) (hash[i] >>> 8);
			out[(4 * i) + 1] = (byte) (hash[i] >>> 16);
			out[(4 * i) + 0] = (byte) (hash[i] >>> 24);
		}

		return out;
	}

	/* constructor for testing purposes
	public Sha() {
		byte[] a = new byte[0];

		byte[][] newArray = pad(a);

		int[] hash = hash(newArray);

		byte[] out = new byte[32];

		for (int i = 0; i < 8; i++) {
			out[(4 * i) + 3] = (byte) (hash[i]);
			out[(4 * i) + 2] = (byte) (hash[i] >>> 8);
			out[(4 * i) + 1] = (byte) (hash[i] >>> 16);
			out[(4 * i) + 0] = (byte) (hash[i] >>> 24);
		}

		final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[out.length * 2];
		for (int j = 0; j < out.length; j++) {
			int v = out[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}

		for (int i = 0; i < 64; i++) {
			System.out.print(hexChars[i]);
		}
	}
	*/

	public byte[][] pad(byte[] a) {
		int len = a.length;
		int k = 64 - ((len + 1 + 8) % 64);

		/* size must be a multiple of 64 bytes */

		byte[] pad = new byte[len + 1 + k + 8];

		for (int i = 0; i < len; i++) {
			pad[i] = a[i];
		}

		pad[len] = (byte) (0b10000000);

		long size = len * 8;
		for (int i = 1; i <= 8; i++) {
			pad[pad.length - i] = (byte) (size >>> ((i - 1) * 8));
		}

		int blocks = pad.length / 64;
		byte output[][] = new byte[blocks][64];

		for (int i = 0; i < blocks; i++) {
			for (int j = 0; j < 64; j++) {
				output[i][j] = pad[(64 * i) + j];
			}
		}

		return output;
	}

	public int[] hash(byte input[][]) {

		int h0 = 0x6a09e667;
		int h1 = 0xbb67ae85;
		int h2 = 0x3c6ef372;
		int h3 = 0xa54ff53a;
		int h4 = 0x510e527f;
		int h5 = 0x9b05688c;
		int h6 = 0x1f83d9ab;
		int h7 = 0x5be0cd19;

		for (int i = 0; i < input.length; i++) {
			int[] w = new int[64];

			/* squeeze 4 bytes into 1 int */
			for (int j = 0; j < 64; j++) {
				w[j] = input[i][j];
				if ((j % 4) == 0) {
					w[j] = (w[j] << 24) & 0xff000000;
				} else if ((j % 4) == 1) {
					w[j] = (w[j] << 16) & 0x00ff0000;
				} else if ((j % 4) == 2) {
					w[j] = (w[j] << 8) & 0x0000ff00;
				} else if((j % 4) == 3) {
					w[j] = (w[j] << 0) & 0x000000ff;
				}
			}

			for (int j = 0; j < 16; j++) {
				w[j] = w[4 * j] | w[(4 * j) + 1] | w[(4 * j) + 2] | w[(4 * j) + 3];
			}

			/*
			 * for(int i = 0; i < 16; i++) { System.out.println(Integer.toHexString(w[i]));
			 * }
			 */

			/* fill out the rest of the array */
			for (int j = 16; j < 64; j++) {
				int s0 = (rightRotateInt(w[j - 15], 7)) ^ (rightRotateInt(w[j - 15], 18)) ^ (w[j - 15] >>> 3);
				int s1 = (rightRotateInt(w[j - 2], 17)) ^ (rightRotateInt(w[j - 2], 19)) ^ (w[j - 2] >>> 10);
				w[j] = w[j - 16] + s0 + w[j - 7] + s1;
			}

			/* below is the actual hash function */
			int a = h0;
			int b = h1;
			int c = h2;
			int d = h3;
			int e = h4;
			int f = h5;
			int g = h6;
			int h = h7;

			for (int j = 0; j < 64; j++) {
				int s1 = (rightRotateInt(e, 6)) ^ (rightRotateInt(e, 11)) ^ (rightRotateInt(e, 25));
				int ch = (e & f) ^ ((~e) & g);
				int temp1 = h + s1 + ch + k[j] + w[j];
				int s0 = (rightRotateInt(a, 2)) ^ (rightRotateInt(a, 13)) ^ (rightRotateInt(a, 22));
				int maj = (a & b) ^ (a & c) ^ (b & c);
				int temp2 = s0 + maj;

				h = g;
				g = f;
				f = e;
				e = d + temp1;
				d = c;
				c = b;
				b = a;
				a = temp1 + temp2;
			}

			h0 = h0 + a;
			h1 = h1 + b;
			h2 = h2 + c;
			h3 = h3 + d;
			h4 = h4 + e;
			h5 = h5 + f;
			h6 = h6 + g;
			h7 = h7 + h;
		}
		
		int[] output = { h0, h1, h2, h3, h4, h5, h6, h7 };

		return output;
	}

	public static int rightRotateInt(int input, int num) {
		int temp = (input << (32 - num));
		int output = (input >>> num) | temp;
		return output;
	}

	public void test(byte[] input) {
		int[] w = new int[64];

		for (int i = 0; i < 64; i++) {
			w[i] = input[i];
			if ((i % 4) == 0) {
				w[i] = (w[i] << 24) & 0xff000000;
			} else if ((i % 4) == 1) {
				w[i] = (w[i] << 16) & 0x00ff0000;
			} else if ((i % 4) == 2) {
				w[i] = (w[i] << 8) & 0x0000ff00;
			}
		}

		for (int i = 0; i < 16; i++) {
			w[i] = w[4 * i] | w[(4 * i) + 1] | w[(4 * i) + 2] | w[(4 * i) + 3];
		}

		for (int i = 0; i < 64; i++) {
			System.out.print(Integer.toHexString(w[i]) + ", ");
		}
		System.out.println();
	}
}

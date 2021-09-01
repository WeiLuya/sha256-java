package hash;

public class Login {

	private String username;
	private String password;
	private boolean authenticated = false;
	
	public Login() {
		Pbkdf p = new Pbkdf();
		String hash = ""; //Retrieve hash from database
		int salt = 0; //retrieve salt from database.
		
		if(p.stretch(password, salt, 4096, 2).equals(hash)) {
			authenticated = true;
		} else {
			authenticated = false;
		}
	}
	
	public int userLogin() {
		
		int uid = 1; // get from database. each uid has an associated permissions and grop
		if(authenticated) {
			return uid;
		} else {
			return -1; //no permissions.
		}
	}
}

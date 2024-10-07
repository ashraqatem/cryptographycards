package cryptographycards;

public class SolitaireCipher {
	public Deck key;

	public SolitaireCipher (Deck key) {
		this.key = new Deck(key); // deep copy of the deck
	}

	/* 
	 * TODO: Generates a keystream of the given size
	 */
	public int[] getKeystream(int size) {
		int [] keyStream = new int[size];

		//Deck deck = new Deck();

		for (int i = 0; i < size; i++){
			keyStream[i] = this.key.generateNextKeystreamValue();
		}
		return keyStream;
	}
	/* 
	 * TODO: Encodes the input message using the algorithm described in the pdf.
	 */
	public String encode(String msg) {
		//first remove all non letters and convert to upper case

		msg = msg.replaceAll("[^a-zA-Z]", "" );

		msg = msg.toUpperCase();

		int[] keystream = getKeystream(msg.length()); //  keystream values needed = number of letters in the message

		String encoded = ""; // initilize the encoded message

		for (int i = 0; i < msg.length(); i++) { // for each letter in the message

			int letter = msg.charAt(i) - 'A'; // convert the letter to a number
			int keystreamValue = keystream[i]; // get the keystream value
			int encodedLetter = (letter + keystreamValue) % 26; // add the keystream value to the letter
			encoded += (char) (encodedLetter + 'A'); // convert the number back to a letter
		}
		return encoded;
	}

	/* 
	 * TODO: Decodes the input message using the algorithm described in the pdf.
	 */
	public String decode(String msg) {

		int[] keystream = getKeystream(msg.length()); //  keystream values needed = number of letters in the message

		String decoded = ""; // initilize the encoded message

		for (int i = 0; i < msg.length(); i++) { // for each letter in the message

			int letter = msg.charAt(i) - 'A'; // convert the letter to a number
			int keystreamValue = keystream[i]; // get the keystream value
			int decodedLetter = (letter - keystreamValue  + 26) % 26; // add the keystream value to the letter
			decoded += (char) (decodedLetter + 'A'); // convert the number back to a letter
		}
		return decoded;
	}

}

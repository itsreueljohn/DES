import java.io.*;
import java.nio.file.Files;
import java.math.BigInteger;
import java.util.Scanner;

class des {

	 static int [][][] sboxes = {
	 {{14,4,13,1,2,15,11,8, 3, 10, 6, 12, 5, 9, 0, 7},
	 {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
	 {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
	 {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13},},

	 {{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
	 {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
	 {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
	 {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9},},

	 {{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
	 {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
	 {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
	 {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12},},

	 {{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
	 {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
	 {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
	 {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14},},

	 {{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
	 {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
	 {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
	 {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3},},

	 {{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
    	 {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
    	 {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
    	 {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13},},

	 {{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
    	 {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
    	 {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
    	 {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12},},

	 {{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
    	 {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
    	 {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
    	 {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11},},
	 };

	 static int pboxes[]={16,7,20,21,29,12,28,17,1,15,23,26,5,18,31,10,2,8,24,14,32,27,3,9,19,13,30,6,22,11,4,25};

	
	static String initialVector="abcdefgh";

	 //Expansion from 4 to 6 bits
	static String expansion(String halfblock) 
	{
		String expanded_halfblock = "";

		expanded_halfblock+=halfblock.charAt(31) + halfblock.substring(0,5);
		expanded_halfblock+=halfblock.substring(3,9);
		expanded_halfblock+=halfblock.substring(7,13);
		expanded_halfblock+=halfblock.substring(11,17);
		expanded_halfblock+=halfblock.substring(15,21);
		expanded_halfblock+=halfblock.substring(19,25);
		expanded_halfblock+=halfblock.substring(23,29);
		expanded_halfblock+=halfblock.substring(27,32) + halfblock.charAt(0);

		return expanded_halfblock;
	}
	
	//Substituting with S boxes
	static String substitute(String keymixed_halfblock) 
	{
		String substituted_halfblock="";

		//EVERY byte for 6 bytes
		for(int i=0;i<48;i+=6){
			//FIRST AND LAST BITS
			int row = bitStringToInteger(keymixed_halfblock.charAt(i)+""+keymixed_halfblock.charAt(i+5));
			//CENTRE 4 bits
			int column = bitStringToInteger(keymixed_halfblock.substring(i+1,i+5));
			substituted_halfblock+= convertToBitString(sboxes[i/6][row][column]+"",4,false,true);

		}
	
	    	return substituted_halfblock;
	}
	
	//Mixing the key with the half block
	static String key_mixing(String expanded_halfblock, String subkey)
	{
		// Xor the expanded halfblock with the key here.
		return xorString(expanded_halfblock,subkey);
	}
	
	//Permutation with P boxes
	static String permute(String substituted_halfblock) {
		String permuted_halfblock = "";
		
		for(int i=0;i<32;i++){
			//PERMUTATION WITH PBOXES
			//-1 because boxes are 1-32
			permuted_halfblock+=substituted_halfblock.charAt(pboxes[i]-1);
		}
		return permuted_halfblock;
	}
	
	//Feistel function on halfblock and a subkey
	static String feistel_function(String halfblock, String subkey) // Accepts half a block and outputs half a block
	{
		//EXPANSION
		String encryptedBlock= expansion(halfblock);
		//KEY MIXING
		encryptedBlock=key_mixing(encryptedBlock,subkey);
		//SUBSTITUTION
		encryptedBlock=substitute(encryptedBlock);
		//PERMUTATION
		encryptedBlock=permute(encryptedBlock);

		return encryptedBlock;

	}
	
	//Reading the file character by character
	static String get_text(String filename) throws Exception
	{
		FileReader ff = new FileReader (filename);
		Scanner sc = new Scanner (ff);
		String text="";

		while(sc.hasNext()){
			text+=sc.next();
		}
		return text;
	}

	//Writes text to file
	static void writeText (String text,String filename) throws Exception{ 

		FileWriter ff = new FileWriter(filename);
		for(int i=0;i<text.length();i++){
			ff.write(text.charAt(i));
		}
		ff.flush();
		ff.close();
		System .out.println("Output successfully written to "+filename);
	}
	
	//Derives round subkeys in encryption or decryption mode
	static String[] derive_round_subkeys(String key, boolean decryption) {
	
		String[] round_subkeys = new String[16];

		if (!decryption){
			for(int i=0;i<16;i++){
				//rotate by 16 bits
				key= key.substring(16,56)+key.substring(0,16);
				//first and last 24 bits
				round_subkeys[i]=key.substring(0,24)+key.substring(32,56);

			}
		}else{
			for(int i=15;i>-1;i--){

				//rotate by 16 bits
				key= key.substring(16,56)+key.substring(0,16);
				//first and last 24 bits
				round_subkeys[i]=key.substring(0,24)+key.substring(32,56);

			}
		}
		return round_subkeys;
	}


	static String xorString(String a, String b)
	{
		String toReturn="";
		for(int i=0;i<a.length();i++)
		{
			toReturn+=a.charAt(i) ^ b.charAt(i);
		}
		return toReturn;
	}
	
	// Applies Feistel network on eight bytes block	
	static String feistel_network(String block, String key,boolean decryption_flag) 
	{
		String[] subkeys = derive_round_subkeys(key, decryption_flag);
		String first_halfblock = block.substring(0,32);
		String second_halfblock = block.substring(32,64);

			for(int i=0;i<16;i++){

				String temp = second_halfblock;
				second_halfblock= xorString(first_halfblock,feistel_function(second_halfblock,subkeys[i]));
				first_halfblock=temp;

			}

		return (second_halfblock+first_halfblock);
	}
	
	//Encryption in CBC mode
	static String des_cbc_encrypt(String plaintext, String key ,String initialVector)
	{
		String ciphertext = "";
		String block="";
		String prevcipher="";
		//IM WORKING WITH BIT STRINGS

		for(int i=0;i<plaintext.length();i+=64){

			if(i==0) {
				block=xorString(plaintext.substring(0,64),initialVector);
			}else{
				block=xorString(prevcipher,plaintext.substring(i,i+64));
			}

			prevcipher=feistel_network(block,key,false);
			ciphertext+=prevcipher;

		}


		return ciphertext;
	}
	
	//Decryption in CBC mode
	static String des_cbc_decrypt(String ciphertext, String key,String initialVector)
	{
		String plaintext = "";
		String block="";
		String prevcipher="";
		// Do work here

		for(int i=0;i<ciphertext.length();i+=64){

			block= feistel_network(ciphertext.substring(i,i+64),key,true);

			if(i==0) {
				block=xorString(block,initialVector);
			}else{
				block=xorString(ciphertext.substring(i-64,i),block);
			}
			plaintext+=block;


		}
		return plaintext;
	}
	
	
	static String pad_plaintext(String plaintext) 
	{
		int pad_value = (8 - plaintext.length()%8); // pad with pad_value, pad_value times
		for(int i=0;i<pad_value;i++)
		{
			plaintext+=(pad_value);
		}
		return plaintext;
	}
	
	
	/**
	 * This function is used to convert a given string consisting of characters or integers and converts it into a bit string
	 *  text - the text to convert
	 *  pad_value -  The length to which to pad till
	 *  bitwise - if true, convert the text into its bitstring character by character 
	 *  intAscii - if true, convert integers into their bitstring representing thei ascii values, else to their decimal value
	 *  Note that intAscii is ignored if bitwise is false
	 **/
	public static String convertToBitString(String text,int pad_value, boolean bitwise,boolean intAscii){

		String binary="";
		String temp="";
		//bitwise checks if we need to convert bit by bit or as a whole
		if (bitwise){

			if(intAscii){
				for(int i=0;i<text.length();i++){
					temp=Integer.toBinaryString(text.charAt(i));
					for(int j=0;j<temp.length()%pad_value;j++) temp = "0"+temp;
					binary+=temp;
				}
			}else{

				for(int i=0;i<text.length();i++){
					try{
					temp=Integer.toBinaryString(Integer.parseInt(Character.toString(text.charAt(i))));
					}catch(NumberFormatException e){
					temp=Integer.toBinaryString(text.charAt(i));
					}
					for(int j=0;j<temp.length()%pad_value;j++) temp = "0"+temp;
					binary+=temp;
				}
			}
		} else{
			temp=Integer.toBinaryString(Integer.parseInt(text));
			for(int j=0;j<temp.length()%pad_value;j++) temp = "0"+temp;
			binary+=temp;
		}

		return binary;

	}

	
	// Convert bit string into the character representing its ascii value
	public static String convertFromBitStringToCharacter(String binary){

		String text="";
		for(int i=0;i<binary.length();i+=8){

			text+=(char)Integer.parseInt(binary.substring(i,i+8),2);
			//System.out.println((char)Integer.parseInt(binary.substring(i,i+8),2));

		}
		return text;
	}

	// Convert bitstring to an integer
	public static int bitStringToInteger(String bitstring){
		
		return Integer.parseInt(bitstring,2);

	}

	static void printBytes(String binary){

		for(int i=0;i<binary.length();i+=8){
			if(i%64==0) System.out.println();
			System.out.print(binary.substring(i,i+8)+"\t");
		}
		System.out.println();
	}


	public static String unpad(String binary){

		int pad_value=Integer.parseInt(binary.substring(binary.length()-8,binary.length()),2);
		return binary.substring(0,binary.length()-(pad_value*8));
	}

	public static void main(String[]args) throws Exception
	{
		String plaintext = get_text("plaintext.txt");
		//System.out.println(plaintext);
		String key = get_text("key.txt");

		//Converting to Bit Strings and padding
		plaintext=convertToBitString(pad_plaintext(plaintext),8,true,false);
		//System.out.println(plaintext.length());
		key=convertToBitString(key,8,true,true);
		initialVector=convertToBitString(initialVector,8,true,true);

		//Encryption
		String ciphertext=des_cbc_encrypt(plaintext,key,initialVector);
		//System.out.println(convertFromBitStringToCharacter(ciphertext));
		//Writing into file
		writeText(ciphertext,"ciphertext.txt");
		System.out.println("--------------------------------------");

		//Decryption
		ciphertext=get_text("ciphertext.txt");
		plaintext= (des_cbc_decrypt(ciphertext,key,initialVector));
		plaintext= unpad(plaintext);
		plaintext=convertFromBitStringToCharacter(plaintext);
		System.out.println(plaintext);
		//Write into file
		writeText(plaintext,"plaintext.txt");
		System.out.println("--------------------------------------");


	}

}

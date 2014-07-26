import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class Main {
	static Random r = new Random();
	static ArrayList<String> words = new ArrayList<String>();
	static HashMap<String,ArrayList<String>> families = new HashMap<String,ArrayList<String>>();
	static ArrayList<Character> guessedLetters = new ArrayList<Character>();
	public static void main(String[] args){
		String wordString = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"));
			String line = "";
			while ((line = reader.readLine())!=null){
				words.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Welcome to evil hangman! What length of word would you like? ");
		Scanner intIn = new Scanner(System.in);
		boolean invalidLength = true;
		int length = 0;
		length_loop:
		while (invalidLength){
			length = intIn.nextInt();
			for (int i=0; i<words.size(); i++){
				if (words.get(i).length()==length){
					break length_loop;
				}
			}
			System.out.println("Please enter a valid length");
		}
		
		lenRemove(length);
//		intIn.close();
		int guesses = 7;
		
		for (int i=0; i<length; i++){
			wordString+="_";
		}
		Scanner s = new Scanner(System.in);
		boolean playing = true;
		while (playing){
			System.out.println(words.size());
			if (guesses>0){
				for (int i=0; i<wordString.length(); i++){
					System.out.print(wordString.charAt(i)+" ");
				}
				System.out.println("");
				System.out.println("you have "+guesses+" guesses left. Enter your guess.");
				char guess = s.nextLine().charAt(0);
				while (!((guess >= 'a' && guess <= 'z') || (guess >= 'A' && guess <= 'Z'))){
					System.out.println(guess+" is not a letter!");
					guess = s.nextLine().charAt(0);
				}
				guess = Character.toLowerCase(guess);
				while (guessedLetters.contains(guess)){
					System.out.println("You already guessed "+guess);
					guess = s.nextLine().charAt(0);
				}
				guessedLetters.add(guess);
				makeWordFamilies(guess,length);
				int biggest = -1;
				ArrayList<String> biggestFamily = null;
				for (ArrayList<String> family : families.values()){
					if (family.size()>biggest){
						biggest = family.size();
						biggestFamily = family;
					}
				}
				String underline = underlineString(biggestFamily.get(0),guess);
				words = biggestFamily;
				if (isBlank(underline)){
					System.out.println("Sorry! "+guess+" is not in the word.");
					guesses--;
				}
				else{
					wordString = makeWordString(underline,wordString);
				}
	
				clearFamilies();
			}
			else if (words.size()==1 && wordString==words.get(0)){
				System.out.println("Congrats! you guessed the word!");
//				System.out.println(words.get(0));
				break;
			}
			else{
				System.out.println("Sorry! The word was "+words.get(r.nextInt(words.size())));
				break;
			}
		}
		intIn.close();
		s.close();
	}
	private static void makeWordFamilies(char c,int length){
		for (String word : words){
			String underline = underlineString(word,c);
//			System.out.println(underline);
			ArrayList<String> family = families.get(underline);
			if (family!=null){
				family.add(word);
			}
			else{
				family = new ArrayList<String>();
				family.add(word);
				families.put(underline, family);
			}
		}
	}
	private static String underlineString(String s,char c){
		String result = "";
		for (int i=0; i<s.length(); i++){
			if (s.charAt(i)==c){
				result+=c;
			}
			else{
				result+='_';
			}
		}
		return result;
		
	}
	private static void clearFamilies(){
		families.clear();
	}
	//Get a random word from the ArrayList
	private static String pickWord(){
		return words.get(r.nextInt(words.size()));
	}
	//Removes any words that are not that length from the arraylist
	private static void lenRemove(String word){
		int len = word.length();
//		Create a new list in which we can store valid words. Removing from an arraylist is expensive.
		ArrayList<String> newList = new ArrayList<String>();
		for (String s : words){
			if (s.length()==len){
				newList.add(s);
			}
		}
//		set the words to this new list.
		words = newList;
	}
	private static void lenRemove(int len){
//		Create a new list in which we can store valid words. Removing from an arraylist is expensive.
		ArrayList<String> newList = new ArrayList<String>();
		for (String s : words){
			if (s.length()==len){
				newList.add(s);
			}
		}
//		set the words to this new list.
		words = newList;

	}
	private static boolean isBlank(String underline){
		for (int i=0; i<underline.length(); i++){
			if (underline.charAt(i)!='_'){
				return false;
			}
		}
		return true;
	}
	private static String makeWordString(String a, String b){
		String s = "";
		if (a.length()!=b.length()){
			return null;
		}
		for (int i=0; i<a.length(); i++){
			if (a.charAt(i)=='_'){
				if (b.charAt(i)=='_'){
					s+="_";
				}
				else{
					s+=b.charAt(i);
				}
			}
			else{
				s+=a.charAt(i);
			}
		}
		return s;
	}
	
}

package fr.jfunctest.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * String utilities.
 * 
 * @author gandrieu
 *
 */
public class StringUtil {

	// +++++ STATIC FIELDS
	public static final String VALIDCHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final int VALICHARSNUM = VALIDCHARS.length();
	// ----- STATIC FIELDS

	// +++++ FIELDS
	// ----- FIELDS

	// +++++ CONSTRUCTOR
	private StringUtil(){}
	// ----- CONSTRUCTOR

	// +++++ PUBLIC METHODS
	/**
	 * Repeats the pattern given n times.
	 */
	public static String repeat(String pattern,int times){
		StringBuilder res = new StringBuilder();
		for (int i=0;i<times;i++){
			res.append(pattern);}
		return res.toString();
	}

	/**
	 * Makes an input stream from a String.
	 * 
	 * BytesArrayInputStream is bugged. Use this instead.
	 * 
	 */
	public static InputStream inputStream(final String in){
		return new InputStream(){
			int charindex = 0;
			public synchronized int read() throws IOException {
				if (charindex<in.length()){
					return (int)in.charAt(charindex++);
				}
				else return -1;
			}
		};
	}


	/**
	 * Short desc : Replaces isolated '&' by an equivalent of '&amp;'.
	 * 
	 * Longer desc :
	 * In a string that may contain XML entities, you have some '&' characters
	 * which are not tied to any entity. You want to replace all these '&'
	 * by their corresponding entity (traditionnaly called "&amp;", 
	 * hence the name of the method although this method replace with the 
	 * hexadecimal entity &#x0026;), but only them, and not the ones already
	 * starting an entity.
	 * Replacing by &#x0026; allows not to have to define any entity in the
	 * XML.
	 * 
	 * Example :
	 * 
	 * String in = "Un &eacute; & un &#x00A0; sont sur un bateau";
	 * String out = ampify(in);
	 * assertTrue(out.equals("Un &eacute; &#x0026; un &#x00A0; sont sur un bateau"));
	 * 
	 * This method must be idempotent of order 1 :
	 * 
	 * assertTrue(out.equals(ampify(out)));
	 * 
	 * @param in
	 * @return
	 */
	public static String xmlAmpify(String in){
		
		// This regexp defines all '&' characters not followed by 
		// a valid body of an entity
		String regexp = "&(?!([a-zA-Z]+|#[0-9]+|#x[0-9a-fA-F]+);)";
		
		return in.replaceAll(regexp, furiousXmlEntitify("&"));
		
	}
	
	/**
	 * Creates xml entities for non ascii characters in the given String.
	 */
	public static String xmlEntitify(String in){

		StringBuffer b = new StringBuffer();

		for (int i=0;i<in.length();i++){

			Character c = in.charAt(i);
			if (c<128){
				b.append(c);
			}
			else if (c=='\ufeff'){
				// BOM character, just remove it
			}
			else {
				String cstr = Integer.toHexString(c).toUpperCase();
				while(cstr.length()<4){
					cstr="0"+cstr;
				}
				b.append("&#x");
				b.append(cstr);
				b.append(";");
			}
		}
		return b.toString();
	}

	/**
	 * Furious because changes also the < and > characters.
	 * 
	 * Not to use with whole xml text. Only with text nodes
	 * 
	 * @param in
	 * @return
	 */
	public static String furiousXmlEntitify(String in){

		StringBuffer b = new StringBuffer();

		for (int i=0;i<in.length();i++){

			Character c = in.charAt(i);
			if (c<128 && c!='&' && c!='<' && c!='>' &&  c!='"'){// && c!='\''){
				b.append(c);
			}
			else if (c=='\ufeff'){
				// BOM character, just remove it
			}
			else {
				String cstr = Integer.toHexString(c).toUpperCase();
				while(cstr.length()<4){
					cstr="0"+cstr;
				}
				b.append("&#x");
				b.append(cstr);
				b.append(";");
			}
		}
		return b.toString();
	}

	/**
	 * Takes a String, and wraps it on the given column count :
	 * inserts new lines every time the current line becomes too long.
	 * <br>
	 * BEWARE ! This methods will NOT keep the original spacing 
	 * between words. It will keep new lines and tabulations, though.
	 * 
	 */
	public static String wrap(String s,int cols){
		if (cols<1) cols = 1;

		StringBuffer out = new StringBuffer();
		String[] lines = s.split("\n");
		LINE:for (String line:lines){
			if (line.length()<cols){
				out.append(line);
				continue LINE;
			}
			else{
				String[] words = line.split(" ");
				int cpt = 0;
				while(cpt<words.length){
					String currentLine = "";//words[cpt++];
					while (currentLine.length()<=cols){
						try{
							if (currentLine.length()+words[cpt].length()+1<=cols){
								currentLine+=(currentLine.length()==0?"":" ")+words[cpt++];
							}
							else if (currentLine.length()==0 && words[cpt].length()<=cols){
								currentLine+=words[cpt++];
								continue;
							}
							else if (currentLine.length()==0 && words[cpt].length()>cols){
								String bigword = words[cpt];
								String cut = bigword.substring(0,cols); 
								words[cpt] = bigword.substring(cols);
								currentLine+=cut;
								break;
							}
							else break;
						}
						catch(ArrayIndexOutOfBoundsException e){
							out.append(currentLine);
							out.append("\n");
							continue LINE;
						}
					}
					out.append(currentLine);
					out.append("\n");
				}
			}
		}
		return out.toString();
	}

	/**
	 * Converts an URL to the local file path corresponding to the file described by the URL.
	 */
	public static String urlToLocalPath(URL url){
		String result = url.getFile();
		if (System.getProperty("os.name").toLowerCase().contains("windows")){
			if (result.matches("/[A-Z]:/.*")){
				result = result.substring(1).replaceAll("/", "\\\\");
				result = result.replaceAll("%20"," ");
			}
		}
		else if (System.getProperty("os.name").toLowerCase().contains("unix")
				|| System.getProperty("os.name").toLowerCase().contains("linux")){
			// TODO TEST
			result = result.replaceAll("%20","\\\\ ");
		}
		return result;
	}


	/**
	 * Suppose you have a String and want to make a regexp out of it.
	 * Bad news : it may contain special characters like '.', '[', '$' which
	 * have a special meaning for regexps. So you want to escape them.
	 * Luckily, it's exactly what this method does.<br>
	 * 
	 * Example:<br>
	 * "fileName.ext" -&gt; "fileName\\.ext"<br>
	 * 
	 * @param regexp 
	 * 					String we want to make safe for regexp compilation.
	 * @return a valid regexp with all special characters escaped.
	 * 
	 *  
	 */
	public static String escapeRegexp(String regexp){
		String specChars = "\\$.*+?|()[]{}^";
		String result = regexp;
		for (int i=0;i<specChars.length();i++){
			Character curChar = specChars.charAt(i);
			result = result.replaceAll(
					"\\"+curChar,
					"\\\\" + (i<2?"\\":"") + curChar); // \ and $ must have special treatment
		}
		return result;
	}

	/**
	 * Camel case anY STRING given as InPuT.<br>
	 * returns : <br>
	 * CamelCaseAnyStringGivenAsInput.
	 */
	public static String camelCase(String in) {
		return removeWhiteSpaces(capitalize(in.toLowerCase(), true));
	}

	/**
	 * Capitalize the first letter of a text. Example : capitalize("toto")
	 * returns "Toto"
	 */
	public static String capitalize(String in) {
		return capitalize(in, false);
	}

	/**
	 * Capitalize all words in a text. Result is trimmed, and all sequences of
	 * white characters are replaced by a regular space: " ".
	 * 
	 * @param in
	 * @param all
	 * @return "toto tata" gives "Toto Tata"
	 */
	public static String capitalize(String in, boolean all) {
		if (in == null || in.equals(""))
			return in;
		if (!all)
			return in.substring(0, 1).toUpperCase() + in.substring(1);
		else {
			StringBuffer out = new StringBuffer("");
			for (String word : in.split("\\s+")) {
				out.append(" ").append(capitalize(word));
			}
			return out.toString().trim();
		}
	}

	public static String removeWhiteSpaces(String in) {
		return in.replaceAll("\\s+", "");
	}

	/**
	 * Finds the first occurrence of a regexp in a string.
	 */
	public static boolean contains(String content, String pattern) {
		Pattern p = Pattern.compile(pattern);
		return contains(content, p);
	}

	/**
	 * Finds the first occurrence of a regexp in a string.
	 */
	public static boolean contains(String content, Pattern pattern) {
		Matcher m = pattern.matcher(content);
		return m.find();
	}

	/**
	 * Finds the first occurrence of a regexp group in a string.
	 */
	public static String findGroup(String content, String pattern,
			int group) {
		Pattern p = Pattern.compile(pattern);
		return findGroup(content, p, group);
	}

	/**
	 * Finds the first occurrence of a regexp group in a string.
	 */
	public static String findGroup(String content, Pattern pattern, int group) {
		Matcher m = pattern.matcher(content);
		if (m.find()) {
			return m.group(group);
		}
		return null;
	}

	/**
	 * Finds the first occurrence of a regexp group in a string.
	 */
	public static List<String> findGroups(String content, String pattern) {
		Pattern p = Pattern.compile(pattern);
		return findGroups(content, p);
	}

	/**
	 * Finds the first occurrence of a regexp group in a string.
	 */
	public static List<String> findGroups(String content, Pattern pattern) {
		List<String> result = new ArrayList<String>();
		Matcher m = pattern.matcher(content);
		if (m.find()) {
			int cpt=1;
			int max = m.groupCount();
			while (cpt<=max){
				result.add(m.group(cpt));
				cpt=cpt+1;
			}
		}
		return result;
	}
	/**
	 * Finds the first occurrence of a regexp in a string.
	 */
	public static String findFirst(String content, String pattern) {
		Pattern p = Pattern.compile(pattern);
		return findFirst(content, p);
	}

	/**
	 * Finds the first occurrence of a regexp in a string.
	 */
	public static String findFirst(String content, Pattern pattern) {
		Matcher m = pattern.matcher(content);
		if (m.find()) {
			return content.substring(m.start(), m.end());
		}
		return null;
	}

	/**
	 * Finds all the occurrences of a regexp in a string.
	 */
	public static List<String> findAll(String content, String pattern) {
		Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
		return findAll(content, p);
	}

	/**
	 * Finds all the occurrences of a regexp in a string.
	 */
	public static List<String> findAll(String content, Pattern pattern) {
		ArrayList<String> found = new ArrayList<String>();
		Matcher m = pattern.matcher(content);
		while (m.find()) {
			found.add(content.substring(m.start(), m.end()));
		}
		return found;
	}

	/**
	 * Finds the first occurrence of a regexp in a string.
	 */
	public static String findLast(String content, String pattern) {
		Pattern p = Pattern.compile(pattern);
		return findLast(content, p);
	}

	/**
	 * Finds the first occurrence of a regexp in a string.
	 */
	public static String findLast(String content, Pattern pattern) {
		Matcher m = pattern.matcher(content);
		String found = null;
		while (m.find()) {
			found = content.substring(m.start(), m.end());
		}
		return found;
	}

	/**
	 * Takes the number num, and displays it with size digits.
	 * 
	 * If num has more than size digits, it is truncated on the left :
	 * addZeros(1907,2) -> "07"
	 * 
	 * If it has less, the left-side is filled with '0's : addZeros(777, 8) ->
	 * "00000777"
	 * 
	 * @param num
	 * @param size
	 * @return a String as described above.
	 */
	public static final String zeroFill(int num, int size) {
		return zeroFill(num,size,"0");
	}

	/**
	 * Takes the number num, and displays it with size digits.
	 * 
	 * If num has more than size digits, it is truncated on the left :
	 * addZeros(1907,2) -> "07"
	 * 
	 * If it has less, the left-side is filled with '0's : addZeros(777, 8) ->
	 * "00000777"
	 * 
	 * @param num
	 * @param size
	 * @return a String as described above.
	 */
	public static final String zeroFill(int num, int size, String character) {
		StringBuilder res = new StringBuilder();
		String snum = "" + num;
		for (int i = snum.length(); i < size; i++) {
			res.append(character);
		}
		res.append(snum);
		return res.toString().substring(res.length() - size);
	}

	/**
	 * Takes a String and cuts it or adds spaces to it to make it have a fixed
	 * length.
	 * 
	 * @param in
	 *            The string to cut/expand.
	 * @param length
	 *            The fixed final length
	 * @param alignleft
	 *            true if spaces are to be added on the right in case 'in' is
	 *            shorter than 'length'
	 * @return
	 */
	public static final String fixedLength(String in, final int length,
			boolean alignleft) {
		if (in.length() >= length)
			return in.substring(0, length);
		else {
			while (in.length() < length) {
				in = alignleft ? in + " " : " " + in;
			}
			return in;
		}
	}

	/**
	 * Generates a random string from the VALIDCHARS characters.
	 * 
	 * @param n
	 *            the length of the random string output
	 * @return
	 */
	public static final String randomString(int n) {
		return randomString(n, VALIDCHARS);
	}

	/**
	 * Generates a random string containing only the chars contained in
	 * validChars.
	 * 
	 * @param n
	 *            the length of the random string output
	 * @param validChars
	 *            the list of acceptable chars. There can be several times the
	 *            same character in the sequence, it would multiply its
	 *            probability of selection in the random string by the number of
	 *            times it appears.
	 * @return
	 */
	public static final String randomString(int n, CharSequence validChars) {
		String res = "";
		int maxindex = validChars.length();
		Random rand = new Random();
		for (int i = 0; i < n; i++)
			res += validChars.charAt(rand.nextInt(maxindex));
		return res;
	}

	/**
	 * Handy function to create labels names. Example : 'Et voilÃ  !' would give
	 * 'ET-VOIL'
	 * 
	 * @param s
	 *            a String to be transformed
	 * @return a String with only [A-Z0-9], and words separated by hyphens
	 */
	public static String toUpperCaseName(String s) {
		String res;
		res = s.replaceAll("\\s*&amp;\\s*|\\s*&nbsp;\\s*", "-");
		res = res.replaceAll("\\s*\\W+\\s*|\\s+", "-");
		res = res.replaceAll("^-+|-+$", "");
		return res.toUpperCase();
	}

	public static final String escapeDoubleQuotes(String suspicious) {
		return suspicious.replaceAll("\\\"", "&#34;");
	}

	public static final String escapeQuotes(String suspicious) {
		// First replace all the \ by \\, to avoid a \' replaced by \\'...
		suspicious = suspicious.replaceAll("\\\\", "\\\\\\\\"); 
		// Then replace all the ' by \' , thus a \' will become \\\'
		return suspicious.replaceAll("\\\'", "\\\\\\\'");
	} 

	public static final String escapeQuotes(Object suspicious) {
		return escapeQuotes(suspicious.toString());
	}

	/**
	 * Tells if the input is a valid email address.
	 * 
	 * @param email
	 * @return
	 */
	public static boolean validEmail(String email) {
		String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$";
		return email.matches(regex);
	}

	/**
	 * Mimicking the php implode function. Takes any iterable object, and
	 * implodes its components using their .toString() method, putting the
	 * delimiter delim between each of them.
	 * 
	 * @param strarr
	 * @param delim
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final String implode(Iterable strarr, String delim) {
		String res = "";
		String del = "";
		int i = 0;
		for (Object c : strarr) {
			i++;
			if (i > 1)
				del = delim;
			res += (del + c.toString());
		}
		return res;
	}

	/**
	 * Same as implode(Iterable strarr, String delim), but taking an array
	 * instead of an Iterable object.
	 * 
	 * @param strarr
	 * @param delim
	 * @return
	 */
	public static final String implode(Object strarr[], String delim) {
		String res = "";
		String del = "";
		int i = 0;
		for (Object c : strarr) {
			i++;
			if (i > 1)
				del = delim;
			res += (del + c.toString());
		}
		return res;
	}

	/**
	 * @param string string where to doble the quotes.
	 * @return a string with simple quotes doubled
	 */
	public static String doubleAppos(String string){
		if (string==null)
			return null;
		else
			return string.replaceAll("'", "''");
	}






	private static final String UPPERCASE_ASCII =
		"AEIOU" // grave
		+ "AEIOUY" // acute
		+ "AEIOUY" // circumflex
		+ "AON" // tilde
		+ "AEIOUY" // umlaut
		+ "A" // ring
		+ "C" // cedilla
		+ "OU" // double acute
		;

	private static final String UPPERCASE_UNICODE =
		"\u00C0\u00C8\u00CC\u00D2\u00D9"
		+ "\u00C1\u00C9\u00CD\u00D3\u00DA\u00DD"
		+ "\u00C2\u00CA\u00CE\u00D4\u00DB\u0176"
		+ "\u00C3\u00D5\u00D1"
		+ "\u00C4\u00CB\u00CF\u00D6\u00DC\u0178"
		+ "\u00C5"
		+ "\u00C7"
		+ "\u0150\u0170"
		;

	public static String toUppercaseNoAccents(String txt) {
		if (txt == null) {
			return null;
		} 
		String txtUpper = txt.toUpperCase();
		StringBuilder sb = new StringBuilder();
		int n = txtUpper.length();
		for (int i = 0; i < n; i++) {
			char c = txtUpper.charAt(i);
			int pos = UPPERCASE_UNICODE.indexOf(c);
			if (pos > -1){
				sb.append(UPPERCASE_ASCII.charAt(pos));
			}
			else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	// ----- PUBLIC METHODS

}

package mobi.octodon.TypingTest.Common;

import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import org.anddev.andengine.opengl.font.Font;

/**
 * Created with IntelliJ IDEA.
 * User: kotlyarovsa
 * Date: 24.06.13
 * Time: 0:11
 * To change this template use File | Settings | File Templates.
 */
public class KeyboardHelper {
	private Context context;
	public static StringBuilder InputChars = new StringBuilder(1000);

	public InputMethodManager GetInputManager()	{
		return (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	public char GetTypedChar(KeyEvent event)
	{
		char typedCh;
		String chars = event.getCharacters();
		if(chars != null)
			typedCh = chars.charAt(0);
		else
			typedCh = (char)event.getUnicodeChar();
		return typedCh;
	}


	public static InputMethodManager GetInputManager(Context context)	{
		return (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	public static String GetNormalizedText(Font font, String text, float textWidth) {
		// no need to normalize, its just one word, so return
		if (!text.contains(" "))
			return text;
		String[] words = text.split(" ");
		StringBuilder normalizedText = new StringBuilder();
		StringBuilder line = new StringBuilder();

		for (int i = 0; i < words.length; i++) {
			if (font.getStringWidth((line + words[i])) > (textWidth)) {
				normalizedText.append(line).append('\n');
				line = new StringBuilder();
			}

			if(line.length() == 0)
				line.append(words[i]);
			else
				line.append(' ').append(words[i]);

			if (i == words.length - 1)
				normalizedText.append(line);
		}
		return normalizedText.toString();
	}


	public KeyboardHelper(Context context)
	{
		this.context = context;
	}


	public KeyboardHelper()
	{
	}

    public static int CheckStringWasTyped(String string) {
        int index = InputChars.length() - string.length();
        if (index < 0)
            return -1;
        String subString = InputChars.substring(index, InputChars.length());
        if (string.contentEquals(subString))
            return 1;
        return 0;
    }

    public static String[] GetCenterKeysWords(String locale) {
        if (locale == "ru")
            return new String[]{"намело", "зубы", "юд", "фктъ",};
        return new String[]{"nbhr", "ejo", "ilsz", "gyq"};

    }

    public static String[] GetAllKeysWords(String locale) {
        if (locale == "ru")
            return new String[]{
							"во",
							"на",
							"не",
							"он",
							"что",
							"со",
							"этот",
							"быть",
							"весь",
							"они",
							"она",
							"как",
							"мы",
							"ко",
							"ты",
							"за",
							"тот",
							"но",
							"вы",
							"по",
							"из",
							"об",
							"обо",
							"свой",
							"же",
							"сказать",
							"так",
							"один",
							"вот",
							"который",
							"наш",
							"только",
							"ещё",
							"от",
							"такой",
							"мочь",
							"то",
							"говорить",
							"бы",
							"для",
							"уже",
							"знать",
							"да",
							"какой",
							"когда",
							"другой",
							"первый",
							"ребята",
							"чтобы",
							"день",
							"год",
							"кто",
							"тебя",
							"дело",
							"нет",
							"рука",
							"очень",
							"большой",
							"ну",
							"новый",
							"стать",
							"школа",
							"сам",
							"работа",
							"сейчас",
							"время",
							"человек",
							"идти",
							"если",
							"два",
						};
        return new String[]{
					 "the",
					 "and",
					 "to",
					 "is",
					 "of",
					 "have",
					 "you",
					 "he",
					 "it",
					 "in",
					 "not",
					 "was",
					 "that",
					 "his",
					 "do",
					 "on",
					 "with",
					 "she",
					 "at",
					 "say",
					 "for",
					 "as",
					 "are",
					 "we",
					 "but",
					 "can",
					 "him",
					 "they",
					 "up",
					 "what",
					 "me",
					 "go",
					 "get",
					 "this",
					 "from",
					 "be",
					 "look",
					 "my",
					 "where",
					 "know",
					 "all",
					 "one",
					 "no",
					 "see",
					 "will",
					 "back",
					 "into",
					 "like",
					 "if",
					 "were",
					 "then",
					 "an",
					 "come",
					 "think",
					 "so",
					 "down",
					 "your",
					 "them",
					 "would",
					 "about",
					 "man",
					 "take",
					 "just",
					 "by",
					 "am",
					 "now",
					 "over",
				};
    }


    public static String[] GetKeysPairChars(int pair, String locale) {
        if (pair == 1) {
            if (locale == "ru")
                return new String[]{"в", "ц", "с", "р", "п", "э", "я", "и"};
            return new String[]{"d", "t", "m", "k", "u", "a"};
        }

        if (pair == 2) {
            if (locale == "ru")
                return new String[]{"н", "м", "т", "л", "о", "ю", "е", "а"};
            return new String[]{"n", "b", "h", "r", "e", "j", "o", "i"};
        }

        if (pair == 3) {
            if (locale == "ru")
                return new String[]{"д", "ф", "к", "б", "ы", "ъ", "у", "з"};
            return new String[]{"l", "s", "z", "g", "y", "q"};
        }

        if (pair == 4) {
            if (locale == "ru")
                return new String[]{"г", "ж", "ч", "ш", "й", "х", "ь", "щ"};
            return new String[]{"w", "p", "c", "v", "x", "f"};
        }
        return GetAllKeysChars(locale);
    }

    public static String[] GetAllKeysChars(String locale) {
        if (locale == "ru")
            return new String[]{"н", "м", "т", "л", "о", "ю", "е", "а", "д", "ф", "к", "б", "ы", "ъ", "у", "з",
                    "в", "ц", "с", "р", "п", "э", "я", "и", "г", "ж", "ч", "ш", "й", "х", "ь", "щ"};
        return new String[]{"N", "B", "H", "R", "E", "J", "O", "I", "L", "S", "Z", "G", "Y", "Q",
                "D", "T", "M", "K", "U", "A", "W", "P", "C", "V", "X", "F", "\"", "'"};

    }
}


//a - z
//	 29 - 54
//
//	 "0" - "9"
//	 7 - 16
//
//	 BACK BUTTON - 4
//	 MENU BUTTON - 82
//
//	 UP, DOWN, LEFT, RIGHT
//	 19, 20, 21, 22
//
//	 SELECT (MIDDLE) BUTTON - 23
//
//	 SPACE - 62
//	 SHIFT - 59
//	 ENTER - 66
//	 BACKSPACE - 67
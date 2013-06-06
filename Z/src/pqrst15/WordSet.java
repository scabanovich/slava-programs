package pqrst15;

public class WordSet {
	
	public static String[] getCurrentWords() {
		return //COUNTRIES_2;
		//COUNTRIES; 
		// RUSSIAN_NUMERALS;
		//MONTHS;
		INTERNS;
	}
	
	static String[] words = {
		"ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN",
		"ELEVEN", "TWELVE", "THIRTEEN",
		"FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN"
	};

	static String[] RUSSIAN_NUMERALS = {
		"ODIN", "DVA", "TRI", "4ETYRE", "P&T'", 
		"#EST'", "SEM'", "VOSEM'", "DEV&T'", "DES&T'",
		"DVAD=AT'", "TRID=AT'", "SOROK", //"STO",
//		"SOROKODIN", "SOROKDVA", "SOROKTRI", "SOROKSEM'",
		"STOODIN", "STODVA", "STOTRI", "STOP&T'", "STOSEM'",
		"STOSOROK",
		"DVESTI", 
		"TRISTA", 
//		"P&T'SOT", 
//		"SEM'SOT",
		"TYS&4A", 
		//"TYS&4AODIN", 
//		"TYS&4ADVA", 
//		"TYS&4ATRI", 
//		"TYS&4ASTO", 
//		"TYS&4AP&T'SOT", 
	};

	static int[] WEIGHTS = {
		1, 2, 3, 4, 5,
		6, 7, 8, 9, 10,
		20, 30, 40, //100,
//		41, 42, 43, 47,
		101, 102, 103, 105, 107,
		140,
		200, 
		300, 
//		500, 
//		700,
		1000, 
		//1001, 
		//1002, 1003,
//		1100,
		//1500
	};

/*	
	static String[] RUSSIAN_NUMERALS = {
		"ODIN", "DVA", "TRI", "4ETYRE", "P&T'", 
		"#EST'", "SEM'", "DES&T'",
		"DVAD=AT'", "SOROK", "STO",
		"STOODIN", "STODVA", "STOP&T'", 
		"DVESTI", 
		"TRISTA", 
		"TYS&4A", 
	};
	
	static int[] WEIGHTS = {
		1, 2, 3, 4, 5,
		6, 7, 10,
		20, 40, 100,
		101, 102, 105, 
		200, 
		300, 
		1000, 
	};
*/
	
	static String[] COUNTRIES = {
		"AVSTRI&", "ANGLI&", "ANGOLA", "ARAVI&", "ARGENTINA",
		"BRAZILI&", "GANA", "GOLLANDI&", "IRAN", "ISPANI&",
		"ITALI&", "GERMANI&", "KORE&", "KOSTARIKA", "KOTDIVUAR",
		"MEKSIKA", "PARAGVAY", "POL'#A", "PORTUGALI&",
		"FRAN@I&", "HORVATI&", "4EHI&", "#VEY@ARI&",
		"SERBI&", "S#A", "TOGO", "TRINIDAD", "TUNIS", "UKRAINA",
		"#VE@I&", "~KVADOR", "&PONI&"
	};
	
	static String[] COUNTRIES_2 = {
		"AVSTRI&", "ANGLI&", "ARAVI&", "ARGENTINA",
		"GANA", 
		"ITALI&", "GERMANI&", "KORE&", 
		"MEKSIKA", "PARAGVAY", "POL'#A", 
		"FRAN@I&", "HORVATI&", "4EHI&", "#VEY@ARI&",
		"S#A", "TOGO", "TUNIS", 
		"#VE@I&", "&PONI&"
	};
	
	static String[] MONTHS = {
		"JNVAR'", "FEVRAL'", "MART", "APREL'", "MAY", "IUN'",
		"IUL'", "AVGUST", "SENTJBR'", "OKTJBR'", "NOJBR'", "DEKABR'"
	};
	static int[] MONTHS_WEIGHTS = {
		1,2,3,4,5,6,7,8,9,10,11,12
	};

	static String[] INTERNS = {
		"#ARAKOIS", "OHLOBYSTIN", "PERM&KOVA", "BYKOV", "KAMYNINA", "KISEGA4",
		"ASMUS", "4ERNOUS", "GLINNIKOV", "ROMANENKO",
		"DEM4OG", "KUPITMAN", "SKR&BINA",
		"IL'IN", "LOBANOV", "LEVIN",
		"BA!RON", "RI4ARDS",
	};
	
}

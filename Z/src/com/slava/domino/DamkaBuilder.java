package com.slava.domino;

import java.util.*;

public class DamkaBuilder {
	DamkaField field = new DamkaField(8);
	DamkaState initial = new DamkaState();
	
	int stateCount = 0;
///	Set codes = new HashSet();
	RoundStack stack = new RoundStack();
	short[] codesUsed = new short[DamkaState.CODE_MODULE];
	int[] damkaCountFlag = new int[]{1,1,1,2,2,2,4,4,8,16,32,64,128,256,512,1024,2048,4096,8192,16384,32768,32768,32768};
	
	static int[] WHITE_DIRS = new int[]{0, 1};
	static int[] BLACK_DIRS = new int[]{2, 3};
	static int[] DAMKA_DIRS = new int[]{0, 1, 2, 3};
	
	int maxDamkaCount = 0;
	
	int getCode(DamkaState s) {
		return s.getCode();
	}
	
	public void build() {
		///codes.add(initial.getCode());
		int code = initial.getCode();
		int damkaCount = initial.getDamkaCount();
		codesUsed[code] += damkaCountFlag[damkaCount];
		stack.add(initial);
		int t = 0;
		while(!stack.isEmpty()) {
			processState();
			++t;
			if(t % 100000 == 0) {
				System.out.println("-->" + t + ":" + stateCount + ":" + stack.size());
			}
		}
	}
	
	void processState() {
		if(stack.isEmpty()) return;
		DamkaState current = stack.removeFirst();
		while(stack.size() > 900000) stack.removeFirst();
		byte[] state = current.state;
		if(mustEat(state, current.turn)) return;
		for (int i = 0; i < state.length; i++) {
			byte value = state[i];
			int[] dirs = getDirections(value, current.turn);
			if(dirs == null) continue;
			for (int di = 0; di < dirs.length; di++) {
				int d = dirs[di];
				int j = field.jp[d][i];
				if(value < 2) {					
					if(j < 0 || state[j] != DamkaState.EMPTY) continue;
					byte nv = (field.y[j] == 0 || field.y[j] == field.length - 1) ? (byte)(value + 2) : value;
					DamkaState next = new DamkaState(current, (byte)i, (byte)j, nv);
					if(nv == value && mustEat(next.state, next.turn)) continue;
					add(next);
					if(nv > value) {
						int dc = next.getDamkaCount();
						if(dc > maxDamkaCount) {
							maxDamkaCount = dc;
							System.out.println("-->" + dc);
							field.print(next.state);
							printPath(next.move);
						}
					}
				} else {
					while(j >= 0 && state[j] == DamkaState.EMPTY) {
						DamkaState next = new DamkaState(current, (byte)i, (byte)j, value);
						if(!mustEat(next.state, next.turn)) add(next);
						j = field.jp[d][j];
					}
				}
			}
		}
	}
	
	void add(DamkaState next) {
		//if(codes.contains(next.getCode())) return;
		int code = next.getCode();
		int damkaCode = damkaCountFlag[next.getDamkaCount()];
		if((codesUsed[code] & damkaCode) != 0) return;
		///codes.add(next.getCode());
		codesUsed[getCode(next)] += damkaCode;
		stack.add(next);
		++stateCount;
	}
	
	int[] getDirections(int value, int turn) {
		if(value == turn + 2) return DAMKA_DIRS;
		if(value == turn) return (turn == 0) ? WHITE_DIRS : BLACK_DIRS;
		return null;
	}
	
	boolean mustEat(byte[] state, int turn) {
		for (int i = 0; i < state.length; i++) {
			short value = state[i];
			if(value != turn && value != turn + 2) continue;
			int[] dirs = DAMKA_DIRS;
			for (int di = 0; di < dirs.length; di++) {
				int d = dirs[di];
				int j = field.jp[d][i];
				if(value >= 2) {
					while(j >= 0 && state[j] == DamkaState.EMPTY) j = field.jp[d][j];
				}
				if(j < 0 || state[j] == DamkaState.EMPTY || state[j] % 2 == turn) continue;
				int j1 = field.jp[d][j];
				if(j1 >= 0 && state[j1] == DamkaState.EMPTY) return true;
			}
		}
		return false;
	}
	
	void printPath(DamkaMove s) {
		List moves = new ArrayList();
		while(s != null && s.parent != null) {
			int b = s.begin;
			int e = s.end;
			char bc = (char)(97 + field.x[b]);
			char ec = (char)(97 + field.x[e]);
			String move = "" + bc + "" + (field.y[b] + 1) + " - " + ec + "" + (field.y[e] + 1);
			moves.add(0, move);
			s = s.parent;
		}
		StringBuffer sb = new StringBuffer();
		String[] ms = (String[])moves.toArray(new String[0]);
		for (int i = 0; i < (ms.length + 1) / 2; i++) {
			int c = i + 1;
			int i1 = 2 * i;
			sb.append(c).append(". ");
			sb.append(ms[2 * i]);
			if(2 * i + 1 < ms.length) {
				sb.append("   ");
				sb.append(ms[2 * i + 1]);
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());
	}

	public static void main(String[] args) {
		DamkaBuilder builder = new DamkaBuilder();
		builder.build();
	}
}

/*
-->17
 O   X   X   O  
   O   .   O   o
 O   o   .   o  
   O   .   .   o
 x   O   x   .  
   .   X   x   X
 .   X   X   X  
   O   X   .   X
1. a3 - b4   b6 - a5
2. e3 - d4   d6 - e5
3. d4 - c5   f6 - g5
4. d2 - e3   e7 - f6
5. e3 - d4   g5 - f4
6. b2 - a3   h6 - g5
7. c1 - b2   g7 - h6
8. c5 - d6   f8 - g7
9. d6 - e7   f4 - e3
10. d4 - c5   e3 - d2
11. e7 - f8   g5 - f4
12. f8 - e7   h6 - g5
13. f2 - e3   d2 - c1
14. e3 - d4   c1 - d2
15. g1 - f2   d2 - c1
16. g3 - h4   f4 - e3
17. f2 - g3   e3 - f2
18. e7 - f8   g7 - h6
19. c5 - d6   g5 - f4
20. d4 - c5   h6 - g5
21. d6 - e7   f2 - g1
22. c5 - d6   g1 - e3
23. f8 - g7   e3 - d2
24. e7 - f8   a7 - b6
25. g7 - h6   b6 - c5
26. d6 - e7   c5 - d4
27. h6 - g7   d4 - e3
28. g7 - h6   e3 - f2
29. h6 - g7   f2 - g1
30. g7 - h6   g1 - a7
31. f8 - g7   c7 - b6
32. e7 - f8   b8 - c7
33. f8 - e7   a7 - b8
34. e7 - d6   b6 - c5
35. d6 - e7   c5 - d4
36. e7 - f8   d4 - e3
37. f8 - e7   e3 - f2
38. e7 - f8   f2 - g1
39. f8 - e7   g1 - a7
40. e7 - f8   d2 - e3
41. f8 - d6   c7 - b6
42. d6 - e7   b8 - c7
43. g7 - f8   a7 - b8
44. e7 - d6   b6 - c5
45. d6 - e7   c5 - d4
46. h6 - g7   c7 - b6
47. b4 - c5   b6 - c7
48. c5 - b6   e3 - d2
49. e7 - b4   f4 - e3
50. f8 - e7   e3 - f2
51. e7 - d6   g5 - f4
52. d6 - c5   f4 - e3
53. g7 - h6   f2 - g1
54. c5 - f8   e3 - f2
55. h6 - g7   d2 - g5
56. b4 - e7   c1 - f4
57. a3 - b4   d4 - e3
58. e7 - c5   e3 - d2
59. g7 - h6   d2 - c1
60. b2 - a3   c1 - e3
61. f8 - g7   e3 - d2
62. c5 - d4   c7 - d6
63. b6 - a7   b8 - c7
64. a7 - b8   d2 - c1
65. b8 - a7   d6 - e7
66. d4 - b6   c7 - b8
67. b6 - d4   d8 - c7
68. d4 - b6   e7 - d8
69. b4 - c5   c1 - e3
70. a3 - b4   e3 - d2
71. c5 - d6   d2 - e3
72. d6 - e7   e3 - d2
73. e7 - f8   d2 - e3
74. f8 - c5   e3 - d2
75. c5 - d4   d2 - e3
76. b4 - c5   e3 - c1
77. c5 - d6   c1 - e3
78. d4 - c5   e3 - c1
79. d6 - e7   c1 - e3
80. c5 - d6   e3 - c1
81. b6 - c5   c1 - e3
82. c5 - a3   e3 - c1
83. a7 - c5   c1 - d2
84. c5 - b4   d2 - e3
85. e7 - f8   e3 - b6
86. a3 - c1   b6 - a7
87. a1 - b2   c7 - b6
88. d6 - e7   b8 - c7
89. b2 - a3   a7 - b8
90. e7 - d6   b6 - c5
91. d6 - e7   c5 - d4
92. c1 - b2   d4 - e3
93. b4 - d6   e3 - d2
94. a3 - b4   d2 - c1
95. b2 - a1   c1 - a3
96. b4 - c5   a3 - c1
97. c5 - b6   c1 - a3
98. b6 - a7   a3 - c1
99. d6 - a3   c1 - e3
100. a3 - b2   e3 - d4
101. e7 - a3   d4 - e3
102. f8 - d6   e3 - d4
103. g7 - f8   d8 - e7
104. d6 - b4   c7 - d6
105. h6 - g7   f4 - e3
106. g7 - h6   g5 - f4
107. f8 - g7   b8 - c7
108. a7 - b8   d4 - b6
109. g7 - f8   c7 - d8
110. h6 - g7   e3 - c5
111. b8 - a7   f4 - c1
112. a7 - b8   c1 - e3
113. g7 - h6   e3 - d4
114. h6 - c1   b6 - c7
115. e1 - d2   f2 - e1
116. f8 - g7   e7 - f8
117. g7 - h6   d8 - e7
118. h6 - g5   c5 - b6
119. g5 - h6   c7 - d8
120. h6 - g5   b6 - c7
121. g5 - h6   d4 - b6
122. h6 - g5   g1 - c5
123. g5 - h6   h8 - g7
124. h6 - g5   g7 - h6
125. g5 - f4   f8 - g7
126. b8 - a7   e7 - f8
127. a7 - b8   g7 - h8
128. b8 - a7   d8 - e7
129. a7 - b8   c7 - d8
130. b8 - a7   f8 - g7
131. a7 - b8   e7 - f8
132. b8 - a7   d8 - e7
133. a7 - b8   c5 - g1
134. b8 - a7   b6 - d8
135. a7 - b8   g1 - b6
136. b8 - a7   b6 - c7
137. f4 - e3   c7 - b8
138. e3 - g1   b8 - c7
139. a7 - f2   h6 - g5
140. d2 - e3   g7 - h6
141. e3 - d4   c7 - b6
142. c1 - e3   b6 - c7
143. d4 - c5   f8 - g7
144. e3 - d4   g5 - f4
145. c5 - b6   f4 - e3
146. d4 - c5   e3 - d2
147. b6 - a7   d2 - c1
148. a7 - b8
*/
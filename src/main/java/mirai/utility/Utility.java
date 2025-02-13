package mirai.utility;

/**
 * A utility class to implement utility methods that other classes may use.
 */
public class Utility {
    /**
     * Recursively calculates the edit distance between the substrings [0, i1) of word 1 and [0, i2) of word 2.
     * This is a helper method to the dynamic programming approach of the getEditDistance method.
     *
     * @param word1 The first word
     * @param word2 The second word
     * @param i1 The end index of the [0, i1) substring of word 1
     * @param i2 The end index of the [0, i2) substring of word 2
     * @param dp The 2-dimensional array for dynamic programming
     * @return The edit distance for the substrings [0, i1) of word and [0, i2) of word2
     */
    private static int getEditDistanceHelper(String word1, String word2, int i1, int i2, int[][] dp) {
        if (i1 < 0 || i2 < 0) {
            return word1.length() + word2.length();
        }

        if (dp[i1][i2] != -1) {
            return dp[i1][i2];
        }

        if (word1.charAt(i1) == word2.charAt(i2)) {
            dp[i1][i2] = getEditDistanceHelper(word1, word2, i1 - 1, i2 - 1, dp);
            return dp[i1][i2];
        }

        int distance = Math.min(getEditDistanceHelper(word1, word2, i1 - 1, i2, dp) + 1,
                Math.min(getEditDistanceHelper(word1, word2, i1, i2 - 1, dp) + 1,
                        getEditDistanceHelper(word1, word2, i1 - 1, i2 - 1, dp) + 1
                )
        );

        dp[i1][i2] = distance;
        return distance;
    }

    /**
     * Return the edit distance between two words.<br><br>
     *
     * The <b>edit distance</b> between two words is the smallest number of
     * operations on the first word to get to the second word. The operations include:<br>
     * <ul>
     *     <li>Replace a letter of the original word with another letter.</li>
     *     <li>Remove a letter from the original word.</li>
     *     <li>Add a letter to the original word.</li>
     * </ul>
     *
     * @param word1 The first word
     * @param word2 The second word
     * @return The edit distance between two words
     */
    public static double getEditDistance(String word1, String word2) {
        int l1 = word1.length();
        int l2 = word2.length();

        word1 = " " + word1;
        word2 = " " + word2;

        int[][] dp = new int[l1 + 1][l2 + 1];

        for (int r = 0; r <= l1; r++) {
            for (int c = 0; c <= l2; c++) {
                dp[r][c] = r == 0
                        ? c
                        : c == 0
                        ? r
                        : -1;
            }
        }

        return getEditDistanceHelper(word1, word2, l1, l2, dp);
    }
}

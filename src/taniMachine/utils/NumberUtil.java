package taniMachine.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * 数値ユーティリティ
 */
public class NumberUtil {
    /** カンマ区切り形式 */
    private static final NumberFormat nfNum = NumberFormat.getNumberInstance();
    /** 通貨形式（日本円） */
    private static final NumberFormat nfCur = NumberFormat.getCurrencyInstance(Locale.JAPAN);

    /**
     * 数値チェック
     * @param num 文字列
     * @return boolean true:数値、false:数値以外
     */
    public static boolean isNumber(String num) {
        boolean ret = true;
        try {
            // Integer変換
            Integer.parseInt(num);
        } catch (Exception e) {
            // 数値ではない
            ret = false;
        }
        return ret;
    }

    /**
     * 通貨変換
     * @param num 数値
     * @return String 通貨変換された文字列
     */
    public static String formatCurrency(int num) {
        return nfCur.format(num);
    }

    /**
     * カンマ区切り変換
     * @param num 数値
     * @return String 通貨変換された文字列
     */
    public static String formatNumber(int num) {
        return nfNum.format(num);
    }
}

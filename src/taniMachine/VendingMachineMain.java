package taniMachine;

import taniMachine.exec.VendingMachine;

/**
 * 自動販売機メインクラス
 * @author Kazunori.Tani
 */
public class VendingMachineMain {

    /** 商品配列（NO、名前、金額） */
    public static Object[][] GOODS_LIST = {
            { "1", "麦茶", 130 },
            { "2", "水", 110 },
            { "3", "コーラ", 160 },
            { "4", "緑茶", 140 },
            { "5", "ビール", 300 }
    };

    /**
     * メインクラス
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("************************************");
        System.out.println("****** 自動販売機を起動します ******");
        System.out.println("************************************");
        System.out.println("");
        // 自動販売機を起動する。
        VendingMachine vm = new VendingMachine(GOODS_LIST);
        vm.purchaseGoods();
        System.out.println("");
        System.out.println("************************************");
        System.out.println("****** 自動販売機を終了します ******");
        System.out.println("************************************");
    }

}

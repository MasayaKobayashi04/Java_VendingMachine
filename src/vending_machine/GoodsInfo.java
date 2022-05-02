package vending_machine;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * 商品情報クラス
 */
class GoodsInfo {

	/**
	 * メンバ変数等
	 */
	private String g1name = " 麦茶 "; private int g1price = 130; private int g1stock;
	private String g2name = "  水  "; private int g2price = 110; private int g2stock;
	private String g3name = "コーラ"; private int g3price = 160; private int g3stock;
	private String g4name = " 緑茶 "; private int g4price = 140; private int g4stock;
	private String g5name = "ビール"; private int g5price = 300; private int g5stock;
	private ArrayList<String> list = new ArrayList<>();

	/**
	 * デフォルトコンストラクタ 初期呼び出し時に在庫数をランダムで生成(0~9) 同時に各商品ごとに「名前」「価格」「在庫数」を配列化
	 * 配列化した要素を商品番号と同時にHashMapに追加
	 */
	GoodsInfo() {
		Random rand = new Random(); // Randomをインスタンス化

		this.g1stock = rand.nextInt(10); // 商品1の在庫情報をランダム化
		String g1arr[] = { g1name, String.valueOf(g1price), String.valueOf(g1stock) }; // 配列に商品情報を格納
		list.add(Arrays.toString(g1arr)); // 配列をリストに格納

		this.g2stock = rand.nextInt(10);
		String g2arr[] = { g2name, String.valueOf(g2price), String.valueOf(g2stock) };
		list.add(Arrays.toString(g2arr));

		this.g3stock = rand.nextInt(10);
		String g3arr[] = { g3name, String.valueOf(g3price), String.valueOf(g3stock) };
		list.add(Arrays.toString(g3arr));

		this.g4stock = rand.nextInt(10);
		String g4arr[] = { g4name, String.valueOf(g4price), String.valueOf(g4stock) };
		list.add(Arrays.toString(g4arr));

		this.g5stock = rand.nextInt(10);
		String g5arr[] = { g5name, String.valueOf(g5price), String.valueOf(g5stock) };
		list.add(Arrays.toString(g5arr));
	}

	/**
	 * 全商品情報を表示するメソッド
	 */
	void displayGoodsInfoAll() {
        NumberFormat nfCur = NumberFormat.getCurrencyInstance();  //通貨形式
		for (int i = 0; i < list.size(); i++) { // listのサイズ分ループ、カウンタは一回ごとに1増やす
			String info = list.get(i); // infoにlistの値を代入(インデックス指定はi変数)
			String arr[] = info.replaceAll("\\[", "").replaceAll("\\]", "").split(", "); // arrにinfoの値をばらして代入
			System.out.println((i + 1) + ":" + arr[0] + "…" + nfCur.format(Integer.parseInt(arr[1])) + "    在庫数…" + arr[2]); //表示
		}
	}

	/**
	 * index番号で呼び出された商品の情報を返すメソッド
	 * 
	 * @param num (index番号)
	 * @return info (商品情報)
	 */
	String getGoodsInfo(int num) {
		String info = list.get(num); // infoにlistのデータを代入(インデックス指定はnum変数)
		return info; // 返却
	}

	/**
	 * データを更新するメソッド
	 * 
	 * @param num  (index番号)
	 * @param data (商品情報)
	 */
	void overWriteGoodsDate(int num, String data) {
		list.set(num, data); // listに上書き(インデックス番号はnum変数、値はdata変数
	}

	/**
	 * ArrayListのSizeを返すメソッド
	 * 
	 * @return size (listのsizeの値)
	 */
	int getListSize() {
		int size = list.size(); // listのサイズをsizeに代入
		return size; // 返却
	}
	
	/**
	 * 在庫商品の最低価格を返すメソッド
	 * @return under_price (最低価格)
	 */
	int returnGoodsUnderPrice() {
		int price[] = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			String info = list.get(i);
			String arr[] = info.replaceAll("\\[", "").replaceAll("\\]", "").split(", ");
			price[i] = Integer.parseInt(arr[1]);
		}
		int under_price = price[0];
		for (int i = 1; i < price.length; i++) {
			if (under_price > price[i]) {
				under_price = price[i];
			}
		}
		return under_price;
	}
	
	/**
	 * 全商品の在庫が0になってないか確認するメソッド
	 * @return result (全部0ならtrue)
	 */
	boolean returnGoodsStock() {
		boolean result = false;
		for (int i = 0; i < list.size(); i++) {
			String info = list.get(i);
			String arr[] = info.replaceAll("\\[", "").replaceAll("\\]", "").split(", ");
			if (Integer.parseInt(arr[2]) == 0 && (i + 1) == list.size()) {
				result = true;
				break;
			} else {
				result = false;
				continue;
			}
		}
		return result;
	}
}
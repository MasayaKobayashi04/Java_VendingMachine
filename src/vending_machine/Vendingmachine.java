package vending_machine;

import java.text.NumberFormat;
import java.util.Scanner;

/**
 * 自販機クラス
 */
public class Vendingmachine {

	/**
	 * メンバ変数等
	 */
	private int num; // インデックス番号のメンバ変数
	private int balance; // ICカード残高のメンバ変数
	private int list_size; // ArrayList.sizeのメンバ変数
	private boolean check; // 商品情報が存在するかどうかの判定を行うメンバ変数
	private String info; // 商品情報を一時的に保管するメンバ変数
	private boolean result;
	private int under_price;

	/**
	 * デフォルトコンストラクタ
	 */
	Vendingmachine() {
	}

	/**
	 * ICカードの残高情報を代入するコンストラクタ
	 * 
	 * @param num (ICカード残高)
	 */
	Vendingmachine(int num) {
		this.balance = num;
	}

	/**
	 * 商品関係の処理を行うメソッド
	 * 
	 * @return int balance (ICカードの最終残高を返す)
	 */
	int workVendingMachine() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in); // Scannerをインスタンス化 (変数名sc)
		GoodsInfo gi = new GoodsInfo(); // GoodsInfoをインスタンス化 (変数名gi)
		NumberFormat nfCur = NumberFormat.getCurrencyInstance(); // 通貨形式
		this.list_size = gi.getListSize(); // ArrayListのサイズを代入する
		this.under_price = gi.returnGoodsUnderPrice();

		for (;;) {
			if (balance >= under_price) {
				this.result = gi.returnGoodsStock();
				if (result == false) {
					System.out.println("      いらっしゃいませ      ");
					System.out.println("==========商品一覧==========");

					gi.displayGoodsInfoAll(); // 全商品情報を表示

					System.out.println("============================");

					try {
						System.out.print("商品を選択してください : ");
						this.num = (Integer.parseInt(scanner.nextLine()) - 1); // 入力された数字を-1してnumに代入
						if ((num + 1) > 0) { // num + 1した値が0より大きいかどうか
							if (num < list_size) { // numの値がサイズより小さいかどうか
								this.check = true; // trueを代入
								this.info = gi.getGoodsInfo(num); // infoにArrayListから商品情報を代入
							} else if (num >= list_size) { // サイズより小さい値ではなかった場合
								this.check = false; // falseを代入
								throw new NoGoodsException(); // 例外を投げる
							}
						} else {
							this.check = false; // falseを代入
							throw new NoGoodsException(); // 例外を投げる
						}
					} catch (NumberFormatException e) { // 数字以外で投げられるとNumberFormatExceptionが投げられるのでcatch
						System.out.println("整数で入力してください"); // 表示
					} catch (IndexOutOfBoundsException | NoGoodsException e) { // 数値がおかしい場合
						System.out.println("その商品は存在しません"); // 表示
					}

					if (check == true) { // checkがtrueの場合のみ実行 = 商品が存在する場合のみ実行
						String arr[] = info.replaceAll("\\[", "").replaceAll("\\]", "").split(", "); // 配列に商品情報を代入
						if (balance >= Integer.parseInt(arr[1])) { // 残高が商品価格と同じか、それ以上あるか確認
							if (Integer.parseInt(arr[2]) > 0) { // 商品が0より大きい場合に実行 (1以上)
								System.out.println("\r\n購入商品:" + arr[0]); // 購入した商品の名前を表示
								this.balance = (balance - Integer.parseInt(arr[1])); // 残高 に残高から商品の値段を引いたものを代入
								System.out.println("  残高  :" + nfCur.format(balance)); // 減った分の残高を表示
								arr[2] = String.valueOf(Integer.parseInt(arr[2]) - 1); // 商品在庫を1減らす
								System.out.println("  在庫  :" + arr[2]); // 減った在庫数を表示
								this.info = ("[" + arr[0] + ", " + arr[1] + ", " + arr[2] + "]"); // 購入後の商品情報を文字列化
								gi.overWriteGoodsDate(num, info); // 購入後商品情報をArrayListに投げて更新
							} else {
								System.out.println("商品在庫がありません"); // 在庫が無い場合は表示
							}
						} else {
							System.out.println("残高が足りません"); // 残高が足りない場合は表示
						}

					}
					// 購入した場合も、してない場合も表示
					System.out.println("\r\n続けて購入しますか？");
					System.out.println("  購入する場合は y を入力");
					System.out.println("  購入しない場合はその他のキーを入力");
					if (scanner.nextLine().equals("y")) { // yが入力されたかどうか
						continue; // yであれば再度ループ
					} else {
						System.out.println("\r\nありがとうございました");
						break; // y以外であれば終了
					}
				} else {
					System.out.println("在庫はありません");
					break;
				}
			} else {
				System.out.println("残高が最低金額に足りていません");
				break;
			}
		}
		scanner.close(); // Scannerをclose
		return balance; // 最終の残高情報を返す
	}
}

class NoGoodsException extends RuntimeException {
} // 商品がないよ例外クラス
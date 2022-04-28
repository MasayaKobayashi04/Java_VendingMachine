package vending_machine;

import java.text.NumberFormat;

/**
 * Userクラス(Mainクラス)
 */
public class User {
	int balance; // 残高のメンバ変数

	/**
	 * デフォルトコンストラクタ
	 */
	User() {
	}

	/**
	 * 残高情報に引数を代入
	 */
	User(int num) {
		this.balance = num;
	}

	public static void main(String[] args) {
        NumberFormat nfCur = NumberFormat.getCurrencyInstance();  //通貨形式
		try {
			if (Integer.parseInt(args[0]) >= 0) { // 正の数か負の数か判定、正の数であれば処理続行
				User us = new User(Integer.parseInt(args[0])); // インスタンス化と同時に引数をメンバ変数に代入
				Vendingmachine vm = new Vendingmachine(us.balance); // インスタンス化と同時にメンバ変数を残高として渡す
				us.balance = vm.workVendingMachine(); // 最終的な残高の値をメンバ変数に代入
				System.out.println("残高は" + nfCur.format(us.balance)); // 表示
			} else { // 負の数の場合
				System.out.println("残高は0か正の数を入力してください"); // 表示
			}
		} catch (NumberFormatException e) { // 渡された引数が整数以外
			System.out.println("残高は整数値で入力してください"); // 表示
		}

	}
}
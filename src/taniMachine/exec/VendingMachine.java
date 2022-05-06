package taniMachine.exec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import taniMachine.bean.GoodsBean;
import taniMachine.utils.NumberUtil;
import taniMachine.utils.StringUtil;

/**
 * 自動販売機クラス
 */
public class VendingMachine {

    /** 入力用スキャナ */
    private Scanner scanner = new Scanner(System.in);

    /** 商品マップ */
    private Map<String, GoodsBean> goodsMap = new HashMap<>();

    /** 最小値段 */
    private int minimumPricce = 0;

    /** 残高 */
    private int balance = 0;

    /** 購入履歴リスト */
    private List<GoodsBean> goodsHistory = new ArrayList<>();

    /**
     * コンストラクタ
     * <pre>
     * ※商品一覧があることを前提としているため、コンストラクタで商品一覧を要求。
     * 　→そのため、デフォルトコンストラクタは無し
     * 本当は商品一覧の「goodsList」配列の内容が正しいかチェックが必要であるが手抜きしています。
     * </pre>
     * @param goodsList 商品一覧(2次元配列)
     */
    public VendingMachine(Object[][] goodsList) {
        // 商品の初期情報を商品マップに登録します。
        for (Object[] goods : goodsList) {
            GoodsBean bean = new GoodsBean();
            // ID
            bean.setNo((String) goods[0]);
            // 商品名
            bean.setName((String) goods[1]);
            // 商品値段
            bean.setPrice((int) goods[2]);
            // 在庫数はランダム値
            Random rand = new Random();
            bean.setStock(rand.nextInt(10));
            // 商品を商品マップに設定
            this.goodsMap.put(bean.getNo(), bean);
        }
        // 最小の商品値段を取得
        this.minimumPricce = this.getMinimumPricce();
    }

    /**
     * 自動販売機購入
     * <ol>
     * <li>自動販売機に投入する金額を入力</li>
     * <li>商品一覧表示</li>
     * <li>購入する商品番号を入力</li>
     * <li>商品購入</li>
     * <li>購入履歴表示</li>
     * </ol>
     */
    public void purchaseGoods() {
        this.out("いらっしゃいませ。");
        // 投入金額を取得する。
        this.balance = this.getInputMoney();
        if (this.balance != 0) {
            while (true) {
                // 残価が商品の最小値段より小さい場合は終了
                if (this.balance < this.minimumPricce) {
                    this.out("残高が足りないため終了します。(残高＝" + NumberUtil.formatCurrency(this.balance) + ")");
                    this.out("");
                    break;
                }
                // 商品一覧を表示
                this.showGoodsList();
                // 商品番号を取得する。
                String goodsNo = this.getInputGoodsNo();
                if ("0".equals(goodsNo)) {
                    break;
                }
                // 商品を購入する。
                this.purchase(goodsNo);
                //購入継続確認
                if (!this.isPurchaseContinue()) {
                    break;
                }
            }
            // 購入履歴表示
            this.showGoodsHistory();
            ;
        }
        this.out("ありがとうございました。");
    }

    /**
     * 投入金額取得
     * <ol>
     * <li>標準入力より文字列受付</li>
     * <li>半角数値チェック</li>
     * <li>0～10000の範囲チェック</li>
     * </ol>
     * @return int 投入された金額
     */
    private int getInputMoney() {
        int money = 0;
        while (true) {
            this.out("投入する金額を0～10000で入力してください。");
            this.out("(やめる場合は「0」を入力してください。)");
            // 標準入力
            String input = this.scanner.nextLine();

            // 入力された値が0の場合は終了
            if ("0".equals(input)) {
                return 0;
            }

            // 入力された値が数値であるかチェック
            if (!NumberUtil.isNumber(input)) {
                // 数値じゃない場合は再入力
                this.out("半角数値で入力されていません。（入力値＝" + input + "）");
                this.out("");
                continue;
            }

            // 入力された値が0～10000であるかチェック
            money = Integer.parseInt(input);
            if (money < 0 || 10000 < money) {
                // 0～10000以外の場合は再入力
                this.out("0～10000の範囲で入力されていません。（入力値＝" + input + "）");
                this.out("");
                continue;
            }

            this.out("入力され金額は" + NumberUtil.formatCurrency(money) + "です。");
            this.out("");

            // 入力値が正常のためwhile終了
            return money;
        }
    }

    /**
     * 商品番号取得
     * <ol>
     * <li>標準入力より文字列受付</li>
     * <li>半角数値チェック</li>
     * </ol>
     * @return int 入力された商品番号
     */
    private String getInputGoodsNo() {
        while (true) {
            this.out("購入する商品番号を入力してください。(残高＝" + NumberUtil.formatCurrency(this.balance) + ")");
            this.out("(やめる場合は「0」を入力してください。)");
            // 標準入力
            String input = this.scanner.nextLine();

            // 入力された値が0の場合は終了
            if ("0".equals(input)) {
                return "0";
            }

            // 入力された値が数値であるかチェック
            if (!NumberUtil.isNumber(input)) {
                // 数値じゃない場合は再入力
                this.out("半角数値で入力されていません。（入力値＝" + input + "）");
                this.out("");
                continue;
            }

            this.out("入力され商品番号は" + input + "です。");
            this.out("");

            // 入力値が正常のためwhile終了
            return input;
        }
    }

    /**
     * 商品購入
     * <ol>
     * <li>商品マップの存在チェック</li>
     * <li>商品の在庫チェック</li>
     * <li>残高チェック（購入可否）</li>
     * <li>在庫数減算</li>
     * <li>残高減算</li>
     * <li>購入履歴に登録</li>
     * </ol>
     * @param goodsNo 商品番号
     */
    private void purchase(String goodsNo) {
        GoodsBean goodsBean = null;

        // 商品番号が商品マップに存在するかチェック
        if (!this.goodsMap.containsKey(goodsNo)) {
            // 商品番号が存在しない場合は再入力
            this.out("存在しない商品番号が入力されました。（入力値＝" + goodsNo + "）");
            this.out("");
            return;
        }

        // 商品番号の在庫チェック
        goodsBean = this.goodsMap.get(goodsNo);
        if (goodsBean.getStock() <= 0) {
            // 在庫なし
            this.out("対象の商品は在庫がありません。（入力値＝" + goodsNo + "）");
            this.out("");
            return;
        }

        // 残高チェック
        if (this.balance < goodsBean.getPrice()) {
            // 金額足りない
            this.out("残高がありません。（残高＝" + NumberUtil.formatCurrency(this.balance)
                    + "、購入商品＝" + goodsBean.getNo() + ":" + goodsBean.getName() + ":"
                    + NumberUtil.formatCurrency(goodsBean.getPrice()) + "）");
            this.out("");
            return;
        }

        // 在庫をマイナス
        int beforeStock = goodsBean.getStock();
        goodsBean.setStock(goodsBean.getStock() - 1);
        // 残高マイナス
        int beforeBalance = this.balance;
        this.balance = this.balance - goodsBean.getPrice();

        // 購入商品表示
        this.out("■購入商品");
        this.out("　番号：" + goodsBean.getNo());
        this.out("　商品：" + goodsBean.getName());
        this.out("　金額：" + NumberUtil.formatCurrency(goodsBean.getPrice()));
        this.out("　在庫：" + beforeStock + " -> " + goodsBean.getStock());
        this.out("■残高情報");
        this.out("　残高：" + NumberUtil.formatCurrency(beforeBalance) + " -> " + NumberUtil.formatCurrency(this.balance));
        this.out("");

        // 購入履歴リストに登録
        this.goodsHistory.add(goodsBean.copy());
    }

    /**
     * 購入継続確認
     * <ol>
     * <li>標準入力より文字列受付</li>
     * </ol>
     * @return true:購入継続、false:やめる
     */
    private boolean isPurchaseContinue() {
        this.out("続けて購入しますか？");
        this.out("(購入する場合は「y」。やめる場合は以外を入力してください)");
        // 標準入力
        return "y".equals(this.scanner.nextLine());
    }

    /**
     * 商品一覧表示
     * <ol>
     * <li>商品マップより商品一覧表示</li>
     * </ol>
     */
    private void showGoodsList() {
        this.out("============= 商品一覧 =============");
        this.goodsMap.forEach((key, value) -> {
            // 商品一覧の出力文字列整形
            String goodsOut = String.format(
                    "%s：%s … %s   在庫数 … %d",
                    StringUtil.lpad(value.getNo(), 2),
                    StringUtil.rpad(value.getName(), 4, "　"),
                    StringUtil.rpad(NumberUtil.formatCurrency(value.getPrice()), 5),
                    value.getStock());
            this.out(goodsOut);
        });
        this.out("====================================");
    }

    /**
     * 商品一覧の最小値段取得
     * <ol>
     * <li>商品マップより商品一覧表示</li>
     * </ol>
     * @return int 最小値段
     */
    private int getMinimumPricce() {
        int minimumPrice = 0;
        if (this.goodsMap != null) {
            // Map.Entryのリストを作成する
            List<Map.Entry<String, GoodsBean>> entries = new LinkedList<>(this.goodsMap.entrySet());
            // 値段順に並べ替え
            Collections.sort(entries, (o1, o2) -> o1.getValue().getPrice() - o2.getValue().getPrice());
            minimumPrice = entries.get(0).getValue().getPrice();
        }
        return minimumPrice;
    }

    /**
     * 購入履歴表示
     * <ol>
     * <li>購入履歴リストより購入履歴表示</li>
     * </ol>
     */
    private void showGoodsHistory() {
        int totalAmount = 0;
        this.out("★★★★★★★購入履歴★★★★★★★");
        for (GoodsBean bean : this.goodsHistory) {
            String goodsOut = String.format(
                    "%s：%s：%s",
                    StringUtil.lpad(bean.getNo(), 2),
                    StringUtil.rpad(bean.getName(), 4, "　"),
                    NumberUtil.formatCurrency(bean.getPrice()));
            this.out(goodsOut);
            totalAmount += bean.getPrice();
        }
        this.out("------------------------------------");
        this.out(String.format("  　%s：%s", StringUtil.rpad("合計", 4, "　"), NumberUtil.formatCurrency(totalAmount)));
        this.out(String.format("  　%s：%s", StringUtil.rpad("残高", 4, "　"), NumberUtil.formatCurrency(this.balance)));
        this.out("★★★★★★★★★★★★★★★★★★");
        this.out("");
    }

    /**
     * メッセージ出力
     * @param message メッセージ
     */
    private void out(String message) {
        System.out.println(message);
    }

}

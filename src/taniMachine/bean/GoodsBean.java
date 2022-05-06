package taniMachine.bean;

/**
 * 商品Beanクラス
 * このクラスは商品を管理するだけの箱のクラスです。
 */
public class GoodsBean {

    /** 商品no */
    private String no;

    /** 商品名 */
    private String name;

    /** 値段 */
    private int price;

    /** 在庫数 */
    private int stock;

    /**
     * Beanコピー
     * ※通常はコピーライブラリがあるため、この様な物は作成しない
     * @return GoodsBean コピーされた商品Bean
     */
    public GoodsBean copy() {
        GoodsBean bean = new GoodsBean();
        bean.setNo(this.no);
        bean.setName(this.name);
        bean.setPrice(this.price);
        bean.setStock(this.stock);
        return bean;
    }

    // ================================
    // setter getterのコメントは手抜き
    // ================================

    /**
     * @return no
     */
    public String getNo() {
        return this.no;
    }

    /**
     * @param no セットする no
     */
    public void setNo(String no) {
        this.no = no;
    }

    /**
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name セットする name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return price
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * @param price セットする price
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @return stock
     */
    public int getStock() {
        return this.stock;
    }

    /**
     * @param stock セットする stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

}

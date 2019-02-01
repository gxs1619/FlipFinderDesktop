
public class Item{
    public String name;
    public int id;
    public String description;
    public int sellPrice;
    public int buyPrice;
    public int sellQuantity;
    public int buyQuantity;
    public int margin;
    public String lastUpdated;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public int getSellQuantity() {
        return sellQuantity;
    }

    public void setSellQuantity(int sellQuantity) {
        this.sellQuantity = sellQuantity;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public void setBuyQuantity(int buyQuantity) {
        this.buyQuantity = buyQuantity;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public String toString(){
        return name + "\t" + buyPrice + "\t" + sellPrice + "\t" + sellQuantity + "\t" + buyQuantity + "\t" + margin + "\t" + lastUpdated;
    }
}

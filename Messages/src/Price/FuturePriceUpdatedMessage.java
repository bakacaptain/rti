package Price;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FuturePriceUpdatedMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long orderId;
	
	private BigDecimal price;
	private long quote;
	private long calculatedAt;
	
	public FuturePriceUpdatedMessage(long orderId, BigDecimal price, long quote, long calculatedAt)
	{ 
		this.orderId = orderId;
		this.price = price;
		this.quote = quote;
		this.calculatedAt = calculatedAt;
	}
	
	public long getOrderId()
	{
		return orderId;
	}
	
	public BigDecimal getPrice()
	{
		return price;
	}
	
	public long getQuote()
	{
		return quote;
	}
	
	public long getCalculatedAt()
	{
		return calculatedAt;
	}

	public String toString(){
		return "Future:\n\tid:"+orderId+"\n\tprice:"+price+"\n\tquote:"+quote+"\n\tcalculatedAt:"+(new Date(calculatedAt)).toString();
	}
}

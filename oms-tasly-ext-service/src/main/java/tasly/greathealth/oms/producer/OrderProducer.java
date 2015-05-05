package tasly.greathealth.oms.producer;


public interface OrderProducer
{
	boolean isOnline();

	void produceOrder() throws Exception;

	void stopProduceOrder() throws Exception;
}

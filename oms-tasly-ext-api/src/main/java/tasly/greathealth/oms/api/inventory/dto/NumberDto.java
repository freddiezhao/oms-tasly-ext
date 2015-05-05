package tasly.greathealth.oms.api.inventory.dto;

public class NumberDto
{

	private int availableNumber;
	private int occupyNumber;
	private String stockroomLocation;


	/**
	 * @return the availableNumber
	 */
	public int getAvailableNumber()
	{
		return availableNumber;
	}


	/**
	 * @param availableNumber the availableNumber to set
	 */
	public void setAvailableNumber(final int availableNumber)
	{
		this.availableNumber = availableNumber;
	}


	/**
	 * @return the occupyNumber
	 */
	public int getOccupyNumber()
	{
		return occupyNumber;
	}


	/**
	 * @param occupyNumber the occupyNumber to set
	 */
	public void setOccupyNumber(final int occupyNumber)
	{
		this.occupyNumber = occupyNumber;
	}


	/**
	 * @return the stockroomLocation
	 */
	public String getStockroomLocation()
	{
		return stockroomLocation;
	}


	/**
	 * @param stockroomLocation the stockroomLocation to set
	 */
	public void setStockroomLocation(final String stockroomLocation)
	{
		this.stockroomLocation = stockroomLocation;
	}


	public NumberDto(final int availableNumber, final int occupyNumber, final String stockroomLocation)
	{
		super();
		this.availableNumber = availableNumber;
		this.occupyNumber = occupyNumber;
		this.stockroomLocation = stockroomLocation;
	}

}

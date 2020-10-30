public class ProgressBar {

	private double totalPct;
	private int totalBars;
	private double completed = 0;

	private long currentTime = System.currentTimeMillis();
	private double amountChanged = 1;
	private int timeLeft = -1;

	/**
	 * Class constructor defining the total amount of ticks, and how many bars to show
	 * @param totalPct the total amount of ticks
	 * @param totalBars the amount of bars to show (length of actual bar)
	 * @throws IllegalArgumentException percentage and bars must be over zero
	 */
	public ProgressBar(double totalPct, int totalBars) throws IllegalArgumentException
	{
		if (totalPct <= 0) throw new IllegalArgumentException("Total Percentage must be over 0!");
		if (totalBars <= 0) throw new IllegalArgumentException("Total Bars must be over 0!");

		this.totalPct = totalPct;
		this.totalBars = totalBars;
	}

	/**
	 * Class constructor defining the start ticks, total amount of ticks, and how many bars to show
	 * @param completed the start amount of ticks
	 * @param totalPct totalPct the total amount of ticks
	 * @param totalBars totalBars the amount of bars to show (length of actual bar)
	 * @throws IllegalArgumentException percentage and bars must be over zero, amount completed must be at least one
	 */
	public ProgressBar(double completed, double totalPct, int totalBars) throws IllegalArgumentException
	{
		if (completed < 0) throw new IllegalArgumentException("Amount completed must start at 0 or above!");
		if (totalPct <= 0) throw new IllegalArgumentException("Total percentage must be over 0!");
		if (totalBars <= 0) throw new IllegalArgumentException("Total bars must be over 0!");

		this.totalPct = totalPct;
		this.completed = completed;
		this.totalBars = totalBars;
	}

	/**
	 * This will add specified amount of ticks to the completed amount
	 * @throws IllegalArgumentException ticks required to be at least one
	 * @param pct how many ticks to add
	 * @return current progress bar
	 */
	public ProgressBar addCompleted(double pct) throws IllegalArgumentException
	{
		if (pct <= 0) throw new IllegalArgumentException("You can only add an amount over 0!");

		amountChanged += pct;
		completed += pct;
		return this;
	}

	/**
	 * This will add a singular tick to the completed amount
	 * @return current progress bar
	 */
	public ProgressBar addCompleted()
	{
		amountChanged += 1;
		completed += 1;
		return this;
	}

	/**
	 * This will remove specified amount of ticks of the completed amount
	 * @deprecated suggested to not remove from the bar
	 * @throws IllegalArgumentException can't remove zero or less ticks
	 * @param pct how many ticks to remove
	 * @return current progress bar
	 */
	public ProgressBar removeCompleted(double pct) throws IllegalArgumentException
	{
		if (pct <= 0) throw new IllegalArgumentException("You can only remove an amount over 0!");

		amountChanged += pct;
		completed -= pct;
		return this;
	}

	/**
	 * This will remove a singular tick of the completed amount
	 * @deprecated suggested to not remove from the bar
	 * @return current progress bar
	 */
	public ProgressBar removeCompleted()
	{
		amountChanged += 1;
		completed -= 1;
		return this;
	}

	/**
	 * This will set the amount of the completed task bar to the exact amount passed in
	 * @deprecated suggested to use addCompleted instead
	 * @throws IllegalArgumentException can't set total amount completed to less than zero
	 * @param pct how many ticks to add
	 * @return current progress bar
	 */
	public ProgressBar setCompleted(double pct) throws IllegalArgumentException
	{
		if (pct < 0) throw new IllegalArgumentException("Can't set completed amount to a negative amount!");

		completed = pct;
		return this;
	}

	/**
	 * This will set the total amount of ticks
	 * @deprecated suggested to keep bars total ticks the same after set
	 * @throws IllegalArgumentException ticks required to be at least one
	 * @param pct how many ticks to add
	 * @return current progress bar
	 */
	public ProgressBar setTotalPct(double totalPct) throws IllegalArgumentException
	{
		if (totalPct <= 0) throw new IllegalArgumentException("Total percentage must be above 0!");

		this.totalPct = totalPct;
		return this;
	}

	/**
	 * What percent of the bar is completed
	 * @return percent of bar completed
	 */
	public double getPctComplete()
	{
		double percent = completed / totalPct * 100;
		if (percent > 100) percent = 100;

		return percent;
	}

	/**
	 * The total amount of the ticks in the bar
	 * @return amount of ticks in the bar
	 */
	public double getTotalPct()
	{
		return totalPct;
	}

	/**
	 * Calculates how many bars to show
	 * @return current amount of bars to be shown
	 */
	private int getCurrentBars()
	{
		return (int) ((completed / totalPct) * totalBars);
	}

	/**
	 * Creates the bar string
	 * @return percent of bar completed
	 */
	public String getBar()
	{
		int bars = getCurrentBars();
		double pct = getPctComplete();

		int spacesPct = 4 - String.valueOf(Math.round(pct)).length();

		StringBuilder bar = new StringBuilder(Math.round(pct) + "%");

		for (int i = 0; i < spacesPct; i++)
		{
			bar.append(" ");
		}

		bar.append(" [");

		for (int i = 0; i < totalBars; i++)
		{
			if (i == bars) bar.append(">");
			if (i >= bars) bar.append(" ");
			else bar.append("=");
			if (pct == 100 && i == totalBars - 1) bar.append(">");
		}

		bar.append("] " + (pct >= 100 ? (int) totalPct + "/" + (int) totalPct : (int) completed + "/" + (int) totalPct));

		int spacesRemaining = String.valueOf((int) totalPct + "/" + (int) totalPct).length() - String.valueOf((int) completed + "/" + (int) totalPct).length();

		for (int i = 0; i < spacesRemaining; i++)
		{
			bar.append(" ");
		}

		long timeElapsed = (System.currentTimeMillis() - currentTime);

		if (timeElapsed >= 5000)
		{
			int pctLeft = (int) (getTotalPct() - completed);
			timeLeft = (int) ((timeElapsed / amountChanged) * pctLeft) / 1000;
			currentTime = System.currentTimeMillis();
			amountChanged = 0;
		}

		bar.append(" | Estimated time: " + (timeLeft <= 0 ? "Calculating..." : timeLeft + "s Left") + "\r");

		return bar.toString();
	}

	/**
	 * Prints the bar from {@link ProgressBar#getBar()} to command line
	 */
	public void print()
	{
		System.out.println(getBar());
	}
}

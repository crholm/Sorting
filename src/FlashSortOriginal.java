
/**


 * FlashSort.java - integer version 


 * Translation of Karl-Dietrich Neubert's algorithm into Java by   


 * Rosanne Zhang



 * at www.webappcabaret.com/javachina


 * Timing measurement included


 * Full functional application


 */

public class FlashSortOriginal implements SortingAlgorithm{


	static int   n;
	static int   m;
	static int[] a;
	static int[] l;
	static int anmin;
	static int nmax;

	/**
	 * constructor 
	 * @param size of the array to be sorted
	 */

	@Override
	public int[] sort(int[] subject, int lowerLimit, int upperLimit) {
		a = subject;
		n = subject.length;
		m = n/20;
		l = new int[m];
		anmin = lowerLimit;
		nmax = upperLimit;
		partialFlashSort();
		insertionSort();

		return a;
	}




	@Override
	public String getName() {
		return "FlashSortOriginal";
	}



	/**
	 * Generate the random array
	 */


	/**
	 * Partial flash sort
	 */


	private static void partialFlashSort() 


	{
		int i = 0, j = 0, k = 0;



		anmin = a[0];
        nmax  = 0;


        for (i=1; i < n; i++)


        {
            if (a[i] < anmin) anmin=a[i];
            if (a[i] > a[nmax]) nmax=i;            
        }


        if (anmin == a[nmax]) return;


		double c1 = ((double)m - 1)/(a[nmax] - anmin);


		for (i=0; i < n; i++)


		{
			k=(int)(c1*(a[i] - anmin));
			l[k]++;
		}


		//printArray(l);


		for (k=1; k < m; k++)


		{
			l[k] += l[k-1];
		}


		int hold = a[nmax];
		a[nmax]=a[0];
		a[0]=hold;


		int nmove = 0;
		int flash;
		j=0;
		k=m-1;


		while (nmove < n-1)


		{
			while (j > (l[k]-1))


			{
				j++;
				k = (int)(c1 * (a[j] - anmin));
			}


			flash = a[j];


			while (!(j == l[k]))


			{
				k = (int) (c1 * (flash - anmin));


				hold = a[l[k]-1];
				a[l[k]-1]=flash;
				flash = hold;


				l[k]--;
				nmove++;
			}
		}


		//printArray(a);
	}


	/**
	 * Straight insertion sort
	 */


	private static void insertionSort()


	{
		int i, j, hold;


		for (i=a.length-3; i>=0; i--)


		{
			if (a[i+1] < a[i]) 


			{
				hold = a[i];
				j=i;


				while (a[j+1] < hold) 


				{
					a[j] = a[j+1];
					j++;
				}


				a[j] = hold;
			}
		}


		//printArray(a);
	}







	/**
	 * For checking sorting result and the distribution
	 */


	private static void printArray(int[] ary)


	{
		for (int i=0; i < ary.length; i++) {


			if ((i+1)%10 ==0) 


				System.out.println(ary[i]);


			else


				System.out.print(ary[i] + " ");
		}


		System.out.println();
	}
}
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class QuickSortThreadedHybrid implements SortingAlgorithm{

	private int[] numbers;
	private int number;
	private int numberOfThreads = 6;
	private int threadsRunning = 0;
	private int insertionThreshold = 75;
	
	private QuickSortThreadedHybrid parent;

	private ThreadPoolExecutor tp = new ThreadPoolExecutor(numberOfThreads, numberOfThreads, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	public int[] sort(int[] values, int lowerLimit, int upperLimit) {
		// Check for empty or null array
		parent = this;
		
		if (values ==null || values.length==0){
			return null;
		}
		this.numbers = values;
		number = values.length;
		
		threadsRunning++;
		tp.execute(new Worker(0, number - 1));
	
		while(threadsRunning != 0){
			try {
				synchronized (parent) {
					this.wait();
				}
				//Thread.sleep(10);
			} catch (InterruptedException e) {	}
		}		
		
		return this.numbers;
	}



	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Quick Sort Threaded";
	}


	private class Worker implements Runnable{
		int highRef;
		int lowRef;
		

		public Worker(int low, int high){
			this.lowRef = low;
			this.highRef = high;
			
		}
		@Override
		public void run() {
			quicksort(lowRef, highRef);
			synchronized (parent) {
				threadsRunning--;
				parent.notifyAll();
			}
		}

		private void quicksort(int low, int high) {
			if(high-low < insertionThreshold){
				insertionSort(low, high);
				return;
			}
			
			int i = low, j = high;
			// Get the pivot element from the middle of the list
			int pivot = numbers[low + (high-low)/2];

			// Divide into two lists
			while (i <= j) {
	
				while (numbers[i] < pivot) {
					i++;
				}
		
				while (numbers[j] > pivot) {
					j--;
				}

		
				if (i <= j) {
					swap(i, j);
					i++;
					j--;
				}
			}
			
			// Recursion or Threading
			if (low < j){
				if(threadsRunning < numberOfThreads){
					synchronized (parent) {
						threadsRunning++;
					}
					tp.execute(new Worker(low, j));
				}else{
					quicksort(low, j);
				}
			}
			if (i < high){
				if(threadsRunning < numberOfThreads){
					synchronized (parent) {
						threadsRunning++;
					}
					tp.execute(new Worker(i, high));
				}else{
					quicksort(i, high);
				}
			}
			
		}

		
		public void insertionSort(int low, int high) {
			 for (int i = low; i <= high; i++){
				  int j = i;
				  int B = numbers[i];
				  while ((j > 0) && (numbers[j-1] > B)){
					  numbers[j] = numbers[j-1];
					  j--;
				  }
				  numbers[j] = B;
			  }	
		}
		
		
		private void swap(int i, int j) {
				int temp = numbers[i];
				numbers[i] = numbers[j];
				numbers[j] = temp;		
		}


	}

}

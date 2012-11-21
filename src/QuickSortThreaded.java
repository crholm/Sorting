
public class QuickSortThreaded implements SortingAlgorithm{

	private int[] numbers;
	private int number;
	private int numberOfThreads = 3;
	private int threadsRunning = 0;  

	public int[] sort(int[] values, int lowerLimit, int upperLimit) {
		// Check for empty or null array
		if (values ==null || values.length==0){
			return null;
		}
		this.numbers = values;
		number = values.length;
		
		threadsRunning++;
		new Thread(new Worker(0, number - 1, this)).start();
	
		while(threadsRunning != 0){
			try {
				synchronized (this) {
					this.wait();
				}
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
		QuickSortThreaded parent;

		public Worker(int low, int high, QuickSortThreaded parent){
			this.lowRef = low;
			this.highRef = high;
			this.parent = parent;
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
			int i = low, j = high;
			// Get the pivot element from the middle of the list
			int pivot = numbers[low + (high-low)/2];

			// Divide into two lists
			while (i <= j) {
				// If the current value from the left list is smaller then the pivot
				// element then get the next element from the left list
				while (numbers[i] < pivot) {
					i++;
				}
				// If the current value from the right list is larger then the pivot
				// element then get the next element from the right list
				while (numbers[j] > pivot) {
					j--;
				}

				// If we have found a values in the left list which is larger then
				// the pivot element and if we have found a value in the right list
				// which is smaller then the pivot element then we exchange the
				// values.
				// As we are done we can increase i and j
				if (i <= j) {
					swap(i, j);
					i++;
					j--;
				}
			}
			// Recursion
			if (low < j){
				if(threadsRunning < numberOfThreads){
					threadsRunning++;
					new Thread(new Worker(low, j, parent)).start();
				}else{
					quicksort(low, j);
				}
			}
			if (i < high){
				if(threadsRunning < numberOfThreads){
					threadsRunning++;
					new Thread(new Worker(i, high, parent)).start();
				}else{
					quicksort(i, high);
				}
			}
		}

		private void swap(int i, int j) {
				int temp = numbers[i];
				numbers[i] = numbers[j];
				numbers[j] = temp;		
		}


	}

}

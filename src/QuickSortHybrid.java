
public class QuickSortHybrid implements SortingAlgorithm{
	private int insertionThreshold = 100;
	private int[] numbers;
	private int number;

	public int[] sort(int[] values, int lowerLimit, int upperLimit) {
		// Check for empty or null array
		if (values ==null || values.length==0){
			return null;
		}
		this.numbers = values;
		number = values.length;
		quicksort(0, number - 1);
		return this.numbers;
	}

	private void quicksort(int low, int high) {
		if(high-low < insertionThreshold){
			insertionSort(low, high);
			return;
		}

		int i = low, j = high;

		// Get a good pivot element
		int pivot = numbers[low + (high-low)/2];
		int left = numbers[low];
		int right = numbers[high];

		if(pivot < left && left < right || right < left && left < pivot )
			pivot = left;
		else if(pivot < right && right < left || left < right && right < pivot )
			pivot = right;


		while (i <= j) {

			while (numbers[i] < pivot) {
				i++;
			}

			while (numbers[j] > pivot) {
				j--;
			}


			if (i <= j) {
				exchange(i, j);
				i++;
				j--;
			}
		}
		// Recursion
		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);
	}

	private void exchange(int i, int j) {
		int temp = numbers[i];
		numbers[i] = numbers[j];
		numbers[j] = temp;
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

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "QuickSortHybrid";
	}

}

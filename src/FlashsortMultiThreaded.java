
public class FlashsortMultiThreaded implements SortingAlgorithm{

	private int numberOfThreads = 6;
	private int threadsRunning = 0; 
	private int[] subject;
	private int result[];
	private int len;
	private double a_min;
	private double a_range;
	
	@Override
	public int[] sort(int[] subject, int lowerLimit, int upperLimit) {
		this.subject = subject;
		result = new int[subject.length];
		
		
		
		len = subject.length;
		
		
		a_min = lowerLimit;
		a_range = upperLimit - lowerLimit;
		
		int increment = (int)((double)len/(double)numberOfThreads)+1;
		
		for(int i = 0; i < numberOfThreads; i++){
			int start = i*increment;
			int end = start + increment - 1;
			if(end >= len)
				end = len-1;
			
			new Thread(new Worker(start, end, this)).start();
			threadsRunning++;
		}
		
		while(threadsRunning != 0){
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {	}
		}		
		
		//long time = System.currentTimeMillis();
		result = new InsertionSort().sort(result, lowerLimit, upperLimit);
		//System.out.println("QuickSort took:" + (System.currentTimeMillis() - time));
		
		
		return result;
	}
	
	
	
	

	@Override
	public String getName() {
		return "Flash Sort Multithreaded";
	}

	
	private class Worker implements Runnable{
		int start;
		int end;
		FlashsortMultiThreaded parent;
		public Worker(int start, int end, FlashsortMultiThreaded parent){
			this.start = start;
			this.end = end;
			this.parent = parent;
		}
		
		
		@Override
		public void run() {
			int index;
			for(int i = start; i < end; i++){
				index = 1+(int)(  ((len-1)*((double)subject[i]-a_min)) / (a_range) );
				add(subject[i], result, index, len);
			}
			threadsRunning--;
			synchronized (parent) {
				parent.notifyAll();
			}
		}
	
		
		private void add(int toBeAdded, int[] result, int index, int len){
			if(result[index] == 0){
				result[index] = toBeAdded;
				return;
			}else if(result[index] <= toBeAdded){
				int i = 1;
				while(index+i < len){
					if(result[index+i] == 0){
						result[index+i] = toBeAdded;
						return;
					}
					i++;
				}
				i=1;
				while(index-i >= 0){
					if(result[index-i] == 0){
						result[index-i] = toBeAdded;
						return;
					}
					i++;
				}		
			}else{
				int i = 1;
				while(index-i >= 0){
					if(result[index-i] == 0){
						result[index-i] = toBeAdded;
						return;
					}
					i++;
				}
				i=1;
				while(index+i < len){
					if(result[index+i] == 0){
						result[index+i] = toBeAdded;
						return;
					}
					i++;
				}
			}
		}
	}
	
}

package mikera.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import mikera.util.emptyobjects.NullArrays;

public class Arrays {
	public static final float[] NULL_FLOATS=NullArrays.NULL_FLOATS;
	public static final int[] NULL_INTS=NullArrays.NULL_INTS;
	public static final byte[] NULL_BYTES=NullArrays.NULL_BYTES;
	public static final double[] NULL_DOUBLES=NullArrays.NULL_DOUBLES;
	
	public static <T extends Comparable<? super T>> boolean isSorted(T[] a, int start, int end) {
		while (start<end) {
			if (a[start].compareTo(a[start+1])>0) return false;
			start++;
		}
		return true;
	}
	
	public static <T extends Comparable<? super T>> boolean isSorted(List<T> a) {
		int length=a.size();
		if (length<=1) return true;
		
		int i=1;
		T previous=a.get(0);
		while (i<length) {
			T current=a.get(i++);
			if (previous.compareTo(current)>0) return false;
			previous=current;
		}
		return true;
	}
	
	public static <T> T[] insertElement(T[] array, T value, int i) {
		int count=array.length;
		@SuppressWarnings("unchecked")
		T[] newarray=(T[]) Array.newInstance(array.getClass().getComponentType(), count+1);
		
		System.arraycopy(array, 0, newarray, 0, i);
		newarray[i]=value;
		System.arraycopy(array, i, newarray, i+1, count-i);

		return newarray;
	}
	

	public static float[] insertElement(float[] array, float value, int i) {
		int count=array.length;
		float[] newarray=new float[count+1];
		
		System.arraycopy(array, 0, newarray, 0, i);
		newarray[i]=value;
		System.arraycopy(array, i, newarray, i+1, count-i);

		return newarray;
	}
	
	public static <T> T[] deleteElement(T[] array, int i) {
		int count=array.length;
		@SuppressWarnings("unchecked")
		T[] newarray=(T[]) Array.newInstance(array.getClass().getComponentType(), count-1);
		
		System.arraycopy(array, 0, newarray, 0, i);
		System.arraycopy(array, i+1, newarray, i, count-i-1);

		return newarray;
	}
	
	public static float[] deleteElement(float[] array, int i) {
		int count=array.length;
		float[] newarray=new float[count-1];;
		
		System.arraycopy(array, 0, newarray, 0, i);
		System.arraycopy(array, i+1, newarray, i, count-i-1);

		return newarray;
	}
	
	public static void swap(int[] data, int a, int b) {
		int t=data[a];
		data[a]=data[b];
		data[b]=t;
	}
	
	public static int[] deduplicate(int[] sortedData) {
		int di=0;
		int si=1;
		while (si<sortedData.length) {
			int v=sortedData[si];
			if (sortedData[di]==v) {
				si++;
			} else {
				sortedData[di+1]=v;
				di++;
				si++;
			}
		}
		di++;
		if (di<sortedData.length) {
			int[] ndata=new int[di];
			System.arraycopy(sortedData, 0, ndata, 0, di);
			return ndata;
		} else {
			return sortedData;
		}
	}
	
	public static float squareDistance(float[] a, float[] b) {
		float eSquared=0;
		for (int i=0; (i<a.length); i++) {
			float d=a[i]-b[i];
			eSquared+=d*d;
		}
		return eSquared;
	}
	
	public static void fillRandom(float[] a) {
		fillRandom(a,0,a.length);
	}
	
	public static void fillRandom(float[] a, int start, int length) {
		for (int i=0; i<length; i++) {
			a[start+i]=Rand.nextFloat();
		}
	}
	
	public static void fillRandom(double[] a) {
		fillRandom(a,0,a.length);
	}
	
	public static void fillRandom(double[] a, int start, int length) {
		for (int i=0; i<length; i++) {
			a[start+i]=Rand.nextDouble();
		}
	}

	public static <T> void swap(List<T> a, int x, int y) {
		T t=a.get(x);
		a.set(x,a.get(y));
		a.set(y,t);
	}
	
	public static <T> void swap(ArrayList<T> a, int x, int y) {
		T t=a.get(x);
		a.set(x,a.get(y));
		a.set(y,t);
	}
	
	public static <T> void swap(T[] a, int x, int y) {
		T t=a[x];
		a[x]=a[y];
		a[y]=t;
	}


	public static <T extends Comparable<? super T>> void mergeInOrder(T[] src, T[] dst, int p1, int p2, int p3, int p4) {
		if (src[p2].compareTo(src[p3])<=0) return; // already sorted!
		
		// cut away ends
		while (src[p1].compareTo(src[p3])<=0) p1++;
		while (src[p2].compareTo(src[p4])<=0) p4--;
		
		int i1=p1;
		int i3=p3;
		int di=p1;
		while(di<p4) {
			if (src[i1].compareTo(src[i3])<=0) {
				dst[di++]=src[i1++];
			} else {
				dst[di++]=src[i3++];
				if (i3>p4) {
					System.arraycopy(src,i1,dst,di,p2-i1+1);
					break;
				}
			}
		}
		
		System.arraycopy(dst, p1, src, p1, (p4-p1)+1);
	}

	public static <T extends Comparable<? super T>> void mergeSort(T[] src, T[] dst, int start, int end) {
		if (start+1>=end) {
			if (start>=end) return;
			if (src[start].compareTo(src[end])>0) {
				swap(src,start,end);
			}
			return;
		}
		
		int middle=(start+end)/2;
		mergeSort(src,dst,start, middle);
		mergeSort(src,dst,middle+1, end);
		mergeInOrder(src,dst,start,middle,middle+1,end);
	}
	
	private static ThreadLocal<Comparable<?>[]> mergeSortTemp=new ThreadLocal<Comparable<?>[]>();
	
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> void mergeSort(T[] src) {
		int length=src.length;
		Comparable<?>[] temp=mergeSortTemp.get();
		if ((temp==null)||(temp.length<length)) {
			temp=new Comparable[length*3/2];
			mergeSortTemp.set(temp);
		}
		mergeSort(src,(T[])temp,0,length-1);
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> al=new ArrayList<Integer>();
		System.out.println(Arrays.isSorted(al));
		al.add(1);
		System.out.println(Arrays.isSorted(al));
		al.add(2);
		System.out.println(Arrays.isSorted(al));
		al.add(10);
		System.out.println(Arrays.isSorted(al));
		al.add(3);
		System.out.println(Arrays.isSorted(al));	
	}

	public static <T> T[] resize(T[] array, int newsize) {
		int len=array.length;
		@SuppressWarnings("unchecked")
		T[] newarray=(T[]) Array.newInstance(array.getClass().getComponentType(), newsize);
		System.arraycopy(array, 0, newarray, 0, Maths.min(newsize,len));
		return newarray;
	}

	public static void zeroFill(float[] array) {
		java.util.Arrays.fill(array, 0);
	}
	
	public static void zeroFill(double[] array) {
		java.util.Arrays.fill(array, 0);
	}

	public static void add(float[] src, float[] dest) {
		for (int i=0; i<src.length; i++) {
			dest[i]+=src[i];
		}
	}
	
	public static void add(double[] src, double[] dest) {
		for (int i=0; i<src.length; i++) {
			dest[i]+=src[i];
		}
	}
	
	public static void add(float[] src, float[] dest, float factor) {
		for (int i=0; i<src.length; i++) {
			dest[i]+=src[i]*factor;
		}
	}
	
	public static void add(double[] src, double[] dest, double factor) {
		for (int i=0; i<src.length; i++) {
			dest[i]+=src[i]*factor;
		}
	}
	
	public static void add(float[] src, int srcOffset, float[] dest, int destOffset, int length, float factor) {
		for (int i=0; i<length; i++) {
			dest[i+destOffset]+=src[i+srcOffset]*factor;
		}
	}
	
	public static void add(double[] src, int srcOffset, double[] dest, int destOffset, int length, double factor) {
		for (int i=0; i<length; i++) {
			dest[i+destOffset]+=src[i+srcOffset]*factor;
		}
	}
	
	public static void multiply(float[] array, float factor) {
		for (int i=0; i<array.length; i++) {
			array[i]*=factor;
		}
	}

	public static void add(float[] array, float value) {
		for (int i=0; i<array.length; i++) {
			array[i]+=value;
		}
	}

	public static boolean checkRange(float[] array, double min, double max) {
		for (int i=0; i<array.length; i++) {
			float v=array[i];
			if ((v<min)||(v>max)) return false;
		}
		return true;
	}
	
	public static boolean checkRange(double[] array, double min, double max) {
		for (int i=0; i<array.length; i++) {
			double v=array[i];
			if ((v<min)||(v>max)) return false;
		}
		return true;
	}

	public static void applySigmoid(float[] data) {
		applySigmoid(data,data.length);
	}
	
	public static void applySigmoid(double[] data) {
		applySigmoid(data,data.length);
	}
	
	public static void applySigmoid(float[] data, int length) {
		for (int i=0; i<length; i++) {
			data[i]=Maths.sigmoid(data[i]);
		}
	}
	
	public static void applySigmoid(double[] data, int length) {
		for (int i=0; i<length; i++) {
			data[i]=Maths.sigmoid(data[i]);
		}
	}
	
	public static void applySigmoid(float[] data, int length, float gain) {
		for (int i=0; i<length; i++) {
			data[i]=Maths.sigmoid(data[i]*gain);
		}
	}
	
	public static void applySigmoid(double[] data, int length, double gain) {
		for (int i=0; i<length; i++) {
			data[i]=Maths.sigmoid(data[i]*gain);
		}
	}
	
	public static void applyTanh(float[] data) {
		applyTanh(data,data.length);
	}
	
	public static void applyTanh(float[] data, int length) {
		for (int i=0; i<length; i++) {
			data[i]=Maths.tanh(data[i]);
		}
	}
	
	public static void applyTanh(double[] data) {
		applyTanh(data,data.length);
	}
	
	public static void applyTanh(double[] data, int length) {
		for (int i=0; i<length; i++) {
			data[i]=Maths.tanh(data[i]);
		}
	}
	
	public static void applyTanhSigmoid(double[] data) {
		applyTanhSigmoid(data,data.length);
	}
	
	public static void applyTanhSigmoid(double[] data, int length) {
		for (int i=0; i<length; i++) {
			data[i]=Maths.tanhSigmoid(data[i]);
		}
	}
	
	public static void applyStochasticSigmoid(float[] data) {
		applyStochasticSigmoid(data,data.length);	
	}


	public static void applyStochasticSigmoid(float[] data, int length) {
		for (int i=0; i<length; i++) {
			float v=data[i];
			if (v<=-30f) {
				data[i]=0.0f;
			} else if (v>=30f) {
				data[i]=1.0f;
			} else {
				data[i]=Rand.nextFloat()<Maths.sigmoid(v)?1:0;
			}
		}	
	}
	
	public static void applyStochasticSigmoid(double[] data, int length) {
		for (int i=0; i<length; i++) {
			double v=data[i];
			if (v<=-30f) {
				data[i]=0.0;
			} else if (v>=30) {
				data[i]=1.0;
			} else {
				data[i]=Rand.nextDouble()<Maths.sigmoid(v)?1:0;
			}
		}	
	}
	
	public static void applyStochasticBinary(float[] data) {
		applyStochasticBinary(data,data.length);
	}

	public static void applyStochasticBinary(float[] data, int length) {
		for (int i=0; i<length; i++) {
			float v=data[i];
			if (v<=0.0f) {
				data[i]=0.0f;
			} else if (v>=1.0f) {
				data[i]=1.0f;
			} else {
				data[i]=Rand.nextFloat()<v?1:0;
			}
		}
	}
	
	public static void applyStochasticBinary(double[] data) {
		applyStochasticBinary(data,data.length);
	}

	public static void applyStochasticBinary(double[] data, int length) {
		for (int i=0; i<length; i++) {
			double v=data[i];
			if (v<=0.0f) {
				data[i]=0.0;
			} else if (v>=1.0) {
				data[i]=1.0;
			} else {
				data[i]=Rand.nextDouble()<v?1:0;
			}
		}
	}

	public static double squaredError(float[] output, float[] result) {
		double err=0;
		for (int i=0; i<output.length; i++) {
			double d=output[i]-result[i];
			err+=d*d;
		}
		return err;
	}
	
	public static double squaredError(double[] output, double[] result) {
		double err=0;
		for (int i=0; i<output.length; i++) {
			double d=output[i]-result[i];
			err+=d*d;
		}
		return err;
	}

	public static void bitsToFloatArray(long val, float[] data, int length) {
		for (int i=0; i<length; i++) {
			data[i]=((val&1)==0)?0.0f:1.0f;
			val = val>>1;
		}
	}
	
	public static void bitsToDoubleArray(long val, double[] data, int length) {
		for (int i=0; i<length; i++) {
			data[i]=((val&1)==0)?0.0:1.0;
			val = val>>1;
		}
	}
	
	/**
	 * Converts an integer to a float array encoding of the integer class number,
	 * i.e. 0 for all values other then the class number, 1 for the correct class
	 * 
	 * @param val
	 * @param data
	 * @param length
	 */
	public static void intToClassArray(int classValue, float[] data, int length) {
		for (int i=0; i<length; i++) {
			data[i]=(classValue==i)?0.0f:1.0f;
		}
	}

	public static boolean contains(int[] array, int value) {
		for (int i=0; i<array.length; i++) {
			if (array[i]==value) return true;
		}
		return false;
	}

	public static void shuffle(int[] is) {
		for (int i=is.length-1; i>=1; i--) {
			int j=Rand.r(i+1);
			if (i!=j) {
				int t=is[i];		
				is[i]=is[j];
				is[j]=t;
			}
		}
	}


}
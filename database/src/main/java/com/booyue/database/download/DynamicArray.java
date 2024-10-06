package com.booyue.database.download;

import android.util.Log;

import com.booyue.base.util.LoggerUtils;


/**
 * 动态数组,结合队列(FIFO)和数组(根据索引进行删除元素)的特性
 * 
 * @author wly
 * 
 */
public class DynamicArray<T> {
    private static final String TAG = "DynamicArray";
    private T[] elems;
    private int mRight; // 右侧有内容索引值，即队列尾
    private int mLeft; // 左侧有内容索引值，即队列首
    private int INCREATE_STEP = 12;
 
    // public static void main(String[] args) {
    // DynamicArray<student> array = new DynamicArray<student>();
    // array.insert(new Student(A));
    // array.insert(new Student(B));
    // array.insert(new Student(C));
    // array.insert(new Student(D));
    // array.insert(new Student(E));
    // array.insert(new Student(F));
    //
    // array.poll();
    // array.peek();
    // array.delete(2);
    // array.getObjectAt(2);
    // System.out.println(array.size());
    //
    // }
    public DynamicArray() {
        elems = (T[]) new Object[INCREATE_STEP];
        mLeft = 0;
        mRight = 0;
    }
 
    /**
     * 插入一个元素到数组
     * 
     * @param t
     */
    public void insert(T t) {
        // 扩展数组
        if (mRight >= elems.length) {
            T[] temp = (T[]) new Object[elems.length + INCREATE_STEP];
            for (int i = 0; i < elems.length; i++) {
                temp[i] = elems[i];
            }
            elems = temp;
            temp = null;
        }
 
        if (elems[mRight] == null) {
            elems[mRight++] = t;
        } else {
            elems[mRight++] = t;
        }
    }
 
    public T peek() {
        if (!isEmpty()) {
            return elems[mLeft];
        }
        return null;
    }
 
    /**
     * 弹出一个元素，将数组起点到p之间的元素都往右移动一位
     * 
     * @return
     */
    public T poll() {
        if (mLeft == mRight) {
            Log.e(TAG ,"数组为空，无法移除");
            return null;
        } else {
            T t = elems[mLeft];
            elems[mLeft++] = null;
            return t;
        }
    }
 
    /**
     * 删除mLeft和mRight之间的元素，从0开始
     * 
     * @param p
     */
    public void delete(int p) {
        p = p + mLeft;
        if (p >= mRight) {
            Log.e(TAG ,"无效的索引值,无法进行删除");
        } else {
            for (int i = p; i > mLeft; i--) {
                elems[i] = elems[i - 1];
            }
            elems[mLeft] = null;
        }
        mLeft++;
    }
 
    /**
     * 返回数组实际保存的有效个数
     * 
     * @return
     */
    public int size() {
        return (mRight - mLeft);
    }
 
    /**
     * 得到mLeft和mRight之间第p个元素，从0开始
     * 
     * @param p
     * @return
     */
    public T getObjectAt(int p) {
        p = p + mLeft;
        if (p >= mRight) {
            LoggerUtils.e(TAG + "无效的索引值,无法进行查找");
            return null;
        } else {
            return elems[p];
        }
    }
 
    /**
     * 数组是否为空
     * 
     * @return
     */
    public boolean isEmpty() {
        return (mRight <= mLeft);
    }

    /**
     * 插入一个元素到数组最左边
     * 
     * @param t
     */
    public void insertLeft(T t) {
    	//左边有空位置，直接添加
    	if(mLeft > 0){
    		elems[--mLeft] = t;
    		return;
    	}
    	
    	//数组为空，直接出插入
    	if( size() == 0 && elems.length > 0 ){
    		insert(t);
    		return;
    	}
    	T[] temp = elems;
        // 扩展数组
        if (mRight >= elems.length) {
            temp = (T[]) new Object[elems.length + INCREATE_STEP];
        }
        
       /**
        * 1）当前数组有足够空间存放新插入的值，则右移数组
        * 2）新分配的空间，从右向左赋值
        */
       for (int i = mRight-1 ; i >= mLeft ; i--) {
            temp[i+1] = elems[i];
        }
       //将插入的值放在最左边
        temp[mLeft] = t;
        elems = temp;
        temp = null;
        //右边界增加
        mRight++;
    }
}

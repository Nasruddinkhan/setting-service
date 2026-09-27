package com.nkhan.configuration;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class Test {
    public static void main(String[] args) {
      //int [] arr = {2, 1, 2, 4, 3};
      //  int [] arr = {5,5,6};
       int [] arr = {3,2,1};
        System.out.println(Arrays.toString(nextGreaterElements(arr)));
        System.out.println(Arrays.toString(nextGreaterStackElements(arr)));
    }

    private static int[] nextGreaterStackElements(int[] nums) {
        Deque<Integer> stack = new ArrayDeque<>();
         int index = 0;
        for (int i = 0; i<nums.length; i++){

        }
   return nums;
    }

    public static int[] nextGreaterElements(int[] nums){
        for (int i = 0; i<nums.length; i++){

           int value =   nextGreaterElements(nums, i);
           nums[i] = value;
        }
        return nums;
    }

    public static int nextGreaterElements(int[] nums, int index){
        int value = -1;
        for (int i = index; i<nums.length; i++){
            if (nums[i] > nums[index]){
                value = nums[i];
                break;
            }
        }
        return value;
    }


}

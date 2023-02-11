// Laura Bedoyan
// Project 1
// 10/7/2022
// COMP 610

//This project takes in a 2d array of N people with men's preferences in the first N/2 rows and women preferences
//in the last N/2 rows. After taking the data input, we split the input array in two arrays-- one of the men's preferences
// and one for the women. Then we run propose dispose algorithm first with the men asking first, then wwith the women
//asking first. We calculate the bias of each proposal type: propose dispose and propose dispose women to see 
//how each run shows a certain preference of choice.

//*************************************************************************
//import necessary libraries
import java.util.*;
import java.io.*;
import java.lang.Math;
import java.util.Arrays;

//class project1
public class Project1 {
//create a static global class constant variable N which will hold the value N of input array.
   static int N = 0;
    
    //Main method:
   public static void main(String[] args) {
   //Get the data from input file
      int[][] theData = getInput();
    //Create the men and women arrays  
      int[][] theWomen = findWomen(theData);
      int[][] theMen = findMen(theData);
     //run pd and pdw 
     int[] menPd = pdAll(theMen, theWomen);
     int[] womenPd = pdAll(theWomen, theMen);
     //print the arrays
     printMArrays(menPd);
     printArrays(womenPd);
     //calculate bias
     int bias = biasCalc(womenPd, menPd, theWomen, theMen );
    }

   // Reading from input.txt and filling theData-- adapted from Professor Noga's Project 0 file. returns the 
   //data in an array theData
   private static int[][] getInput() {
      Scanner sc = null;
      try {
         // Note the filename is input.txt without any mention of the path
         sc = new Scanner(new File("input.txt"));
      } 
      //if file is not found
      catch (FileNotFoundException e) 
      {
         System.out.println("Did you forget the input file?");
         System.exit(1);
      }
      //initialize size to be the first integer from input file
      int pos = 0, size = sc.nextInt(), counter=0;
      N =size;
     //create the neww 2d array of men and women preferences from input file
      int[][] theData = new int[size*2][size];
      
      //add in the data
      while (sc.hasNextInt()) 
      {
         for (int row = 0; row < size*2; row++) 
         { 
            for (int col = 0; col < size; col++)
            {
                 
                 theData[row][col] = sc.nextInt();  
            }
            counter ++;
         } 
      }
    
     return theData;
     }
     
     //takes in the data from input fxn and splits into the women preferences; returns the women preferences
     public static int[][] findWomen(int[][] theData)
     {
     int size = theData[0].length; // N/2
     int[][] theWomen = new int[size][size];
     System.arraycopy(theData, size, theWomen,0, theWomen.length);//copy men from thedata into this new one
     return theWomen;
     }
     
     //takes in the data from input fxn and splits into the women preferences; returns the men preferences
     public static int[][] findMen(int[][] theData)
     
     {
     int size = theData[0].length;// N/2
     int[][] theMen = new int[size][size];
     System.arraycopy(theData, 0 , theMen, 0 , theMen.length); //copy the women into the array from main thedata
     return theMen;
     }
     
     
     //pd adapted from geeks for geeks
     //https://www.geeksforgeeks.org/stable-marriage-problem/
     //method takes in the men and women and returns the stable matchings
     // can run pdw if you switch the wwomen and men when calling the function
     public static int[] pdAll(int[][] theWomen, int[][] theMen)
     
     {   
     //N is N/2
        int N = theWomen[0].length;
        //new array to store the matches. index is the proposer, value is proposed
        int wPartner[] = new int[N]; 
        //boolean array to keep track of who is free or not. Free = false. Not free = true
        boolean mFree[] = new boolean[N]; 
        //initialize the matches to -1       
        Arrays.fill(wPartner, -1); 
        //keep track of when each man gets proposed. N/2 times.
        int freeCount = N;
        
        //while loop ends only when all men are proposedd to
        while (freeCount > 0)
        { 
        
        
            int m; //find the unavailable man-- returns the man index
            
            for (m=0; m<N; m++) //go through the men to find which men is available and needs to find a match
            {
                  if (mFree[m] == false) // if false then we have found the guy in question
                     {break; }
            
            }
            //increase by one because it is by index
            int mi = m +1;
           
            //going through the women to see who is available
            
           for (int i = 0; i < N && mFree[m] == false; i++) //if man is free and we have not gone through all of 
                                                         //his available choices 
           {
               int w = theWomen[m][i]; //woman is equal to next free woman who has not rejected him
               
               //found woman
               int wIndex = w-1; // find the index
            
               if (wPartner[wIndex]==-1) // if the woman is available
               {
               
                     wPartner[wIndex] = m; //make them get engaged-- assign value m to the index of w in wpartner array
                     mFree[m]=true; // update boolean to true because he is not unavailable
                     freeCount--;//decrease free count because now they are matched
                    
               }
               
               else //if his first choice is unavailable, check to see if they actually prefer each other more or not
               {
               
                  int m1 = wPartner[wIndex]; //current dude shes with 
                 int mo = m1 +1; // increase cause index
               
                  if (prefersBool(theMen, w, m, m1) == false) //if false she likes new guy more than her current dude
                { 
                    wPartner[wIndex] = m; //w gets engaged to new guy
                    
                    mFree[m] = true; // new guy is not available
                    
                    mFree[m1] = false; //old guy is
                    
                   
                } 
                
                else
                {
                wPartner[wIndex]=m1;//she stays with current guy
                mFree[m] = false; //he is still unavailable
                mFree[m1] =true;//new guy is free
             
                }// end else
               
             }// end else
               
           } //end for
            
        
        }// end while
     
        return wPartner;
     }
   
     
  // this is also adapted from geeks for geeks
     //https://www.geeksforgeeks.org/stable-marriage-problem/
// method that returns if the woman prefers the new proposal over her current engasement
// m1 is current, w is woman m is old current guy
// returns a boolean
     public static boolean prefersBool(int[][] theMen, int w, int m, int m1)
     {
     m = m+1;
     m1 = m1+1;
     int size = theMen[0].length;
    // System.out.println(Arrays.deepToString(theMen));
     
     boolean bool = false;
      for (int i = 0; i < size; i++) 
    { 
        // If old guy m1 comes before m then w prefs old guy and will stay
      //update boolean return it
      
       
        if (theMen[w-1][i] == m1) 
        {
              
              bool = true;
              break;
              
         }
        // If old guy m1 comes after m then w prefs new guy and will swwitch to him
       if (theMen[w-1][i] == m) 
        {
             bool = false;
             
             break; 
           
        }
    }
    
   return bool;
    
     }
     
    //print array function  
    // also adapted from geeks for geeks.com cited above
     public static void printArrays(int[] wPartner){
     
            for (int i = 0; i < N; i++) 
               {

               int index = wPartner[i] +1;
               int windex = i+1;
                System.out.print(" "); 
                  System.out.println(windex + " "+ index  );

               }

               }
   // print out men arrays            
   public static void printMArrays(int[] wPartner){
     
     for (int x = 0; x < N; x++) 
         {
      
      int man = findIndex(wPartner, x); //the man
      int woman = wPartner[man];
      
      int newman = man+1;
      int newwoman= woman+1;
    
    System.out.print(" "); 
    System.out.println(newwoman+  " "+ newman );

      }
      
   }  
   // find index method to searrch array for inddex of a certain value
     public static int findIndex(int arr[], int t)
    {
         int index = -1;
         
        for(int i = 0; i <arr.length; i++) {
            if(arr[i] == t) {
                index = i;
                break;
            }
        }
        
        return index;
    }
     // find index method to searrch 2d array for inddex of a certain value
    public static int findIndex2d(int arr[][], int t, int x)
    {
         int index = -1;
         
        for(int i = 0; i <arr.length; i++) {
       
                       
            if(arr[x][i] == t) {
                index = i;
               
                break;
            }
        }
        
        return index;
    }
    
    // calculate bias. takes in women pd and men pd arrays and thedata-- women and men split
     public static int biasCalc(int []womenPd, int []menPd, int[][] theWomen, int[][]theMen )
     {
     
     int bias= 0;
     //create two arrays for the biases to keep track- one for men one for women
     int [] biasarrayM = new int[N];
     int [] biasarrayW = new int[N];
   
       for (int r=0; r<N; r++)      
              {
              // find man in women pd array add one because inddex. man is the inddex
              int man = findIndex (womenPd, r); 
                  man = man +1;
               // in this case for men pd men are the value   
                  int man2 = menPd[r];                 
                 man2 = man2+1;
                 // find the men from either pd women or normal pd in the women array to see what place they are in
                  int m1 = findIndex2d(theWomen, man, r);
                  int m2 = findIndex2d(theWomen, man2, r);
                  // take absolute value to find bias for r man
                  biasarrayW[r]=Math.abs(m1-m2);
               
              }// end for
              
              
           //x is woman
             for (int x=0; x<N; x++)      
              {
                   // x is man and woman is who he is paired with
                   // find wowman in man pd array add one because inddex. woman is the inddex

                  int woman = findIndex (menPd, x); 
                  woman = woman +1;
                   // in this case for women pd women are the value  
                  int woman2 = womenPd[x]; 
                  woman2 = woman2+1;
                 
                  //look through women and men 
                  // find the women from either pd women or normal pd in the men array to see what place they are in
                  int w1 = findIndex2d(theMen, woman, x);
                  int w2 = findIndex2d(theMen, woman2, x);
                 // calculate bias
                  biasarrayM[x]=Math.abs(w1-w2);
                 
              }// end for

           for (int i=0; i<N; i++)      
          {
              // calculate bias by addding up all values together in the two arrays
            int n= biasarrayM[i]+  biasarrayW[i];   
            bias = bias +n;
         
          }// end for
           
           System.out.println(" ");
           System.out.println(bias);
           
         //print and return bias
            return bias;
     }
     
     }// end class
   

  
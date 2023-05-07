package getSourceCode21052018;

import java.io.Serializable;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class Order implements Serializable
/*    */ {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*    */   int orderNr;
/*    */   int freq;
/*    */   int volume;
/*    */   double duur;
/*    */   int matrixID;
/*    */   ArrayList<Integer> otherKeys;
/*    */ 
/*    */   public Order(int req, int frq, int vol, double dr, int mID)
/*    */   {
/* 16 */     this.orderNr = req;
/* 17 */     this.freq = frq;
/* 18 */     this.volume = vol;
/* 19 */     this.duur = dr;
/* 20 */     this.matrixID = mID;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 25 */     return " Order: " + this.orderNr + " matrixID: " + this.matrixID + " Freq: " + this.freq + " Volume: " + this.volume + " Duur: " + this.duur + "\n";
/*    */   }
/*    */ }
package getSourceCode21052018;

		 import java.io.Serializable;
/*    */ 
/*    */ public class Counter implements Serializable
/*    */ {
			
/**
	 * 
	 */
		   private static final long serialVersionUID = 1L;
/*    */   int number;
/*    */   String name;
/*    */   Counters counters;
/*    */ 
/*    */   public Counter(int no, String nm, Counters ctrs)
/*    */   {
/* 10 */     this.number = no;
/* 11 */     this.name = nm;
/* 12 */     this.counters = ctrs;
/* 13 */     this.counters.addCounter(this);
/*    */   }
/*    */ 
/*    */   public void reset() {
/* 17 */     this.number = 0;
/*    */   }
/*    */ 
/*    */   public void up() {
/* 21 */     this.number += 1;
/*    */   }
/*    */ 
/*    */   public void print()
/*    */   {
/* 26 */     System.out.println(this.name + ": " + this.number);
/*    */   }
/*    */ }
package getSourceCode21052018;

/*     */ import java.io.PrintWriter;
		  import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class Solution implements Serializable
/*     */ {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*     */   Schedule[][] scheduleKeys;
/*     */   LinkedList<ScheduleEntry> denied;
/*     */   Random r;
/*     */   double decline;
/*     */   public double score;
/*     */   double endTimes;
/*     */   double volScores;
/*     */   boolean spam;
/*     */ 
/*     */   public Solution(LinkedList<ScheduleEntry> dnd, double dcln, boolean spm, Random rr)
/*     */   {
/*  29 */     this.denied = dnd;
/*  30 */     Collections.shuffle(this.denied);
/*  31 */     this.decline = dcln;
/*  32 */     this.score = (this.decline + 25200.0D);
/*  33 */     this.r = rr;
/*  34 */     this.spam = spm;
/*     */ 
/*  36 */     this.scheduleKeys = new Schedule[6][3];
/*  37 */     for (int d = 1; d <= 5; d++) {
/*  38 */       for (int v = 1; v <= 2; v++) {
/*  39 */         Schedule schedule = new Schedule(d, v);
/*  40 */         this.scheduleKeys[d][v] = schedule;
/*     */       }
/*     */     }
/*     */ 
/*  44 */     this.scheduleKeys[1][1].addNewTrip();
/*  45 */     this.scheduleKeys[2][1].addNewTrip();
/*  46 */     this.scheduleKeys[4][1].addNewTrip();
/*  47 */     this.scheduleKeys[5][1].addNewTrip();
/*     */   }
/*     */ 
/*     */   public int toggleVehicle(int vehicle)
/*     */   {
/*  52 */     if (vehicle == 1)
/*  53 */       vehicle = 2;
/*     */     else {
/*  55 */       vehicle = 1;
/*     */     }
/*  57 */     return vehicle;
/*     */   }
/*     */ 
/*     */   public void remove(ScheduleEntry se, int volDiff, double timeDiff)
/*     */   {
/*  63 */     se.previous.next = se.next;
/*  64 */     se.next.previous = se.previous;
/*  65 */     Trip trip = se.trip;
/*     */ 
/*  67 */     Schedule schedule = this.scheduleKeys[se.day][se.vehicle];
/*  68 */     int lastI = trip.size() - 1;
/*  69 */     ScheduleEntry lastSE = (ScheduleEntry)trip.get(lastI);
/*     */ 
/*  71 */     trip.set(se.key, lastSE);
/*  72 */     lastSE.key = se.key;
/*  73 */     trip.remove(lastI);
/*  74 */     schedule.updateTime(timeDiff);
/*  75 */     se.trip.updateVolume(volDiff);
/*     */   }
/*     */ 
/*     */   public void insert(ScheduleEntry se, ScheduleEntry indicator, int volDiff, double timeDiff)
/*     */   {
/*  83 */     se.day = indicator.day;
/*  84 */     se.vehicle = indicator.vehicle;
/*  85 */     se.trip = indicator.trip;
/*     */ 
/*  88 */     indicator.previous.next = se;
/*  89 */     se.previous = indicator.previous;
/*  90 */     se.next = indicator;
/*  91 */     indicator.previous = se;
/*     */ 
/*  93 */     Schedule schedule = this.scheduleKeys[se.day][se.vehicle];
/*  94 */     Trip trip = se.trip;
/*  95 */     se.key = trip.size();
/*  96 */     trip.add(se);
/*     */ 
/*  98 */     schedule.updateTime(timeDiff);
/*  99 */     se.trip.updateVolume(volDiff);
/*     */   }
/*     */ 
/*     */   public void applyTwoOpt(ScheduleEntry se1, ScheduleEntry indic1, ScheduleEntry se2, ScheduleEntry indic2, double timeDiff)
/*     */   {
/* 104 */     se1.next = se2;
/*     */ 
/* 106 */     ScheduleEntry currentSE = se2;
/* 107 */     ScheduleEntry prevSE = se1;
/*     */ 
/* 109 */     while (currentSE != indic1)
/*     */     {
/* 111 */       currentSE.next = currentSE.previous;
/* 112 */       currentSE.previous = prevSE;
/* 113 */       prevSE = currentSE;
/* 114 */       currentSE = currentSE.next;
/*     */     }
/*     */ 
/* 117 */     indic1.previous = prevSE;
/* 118 */     indic1.next = indic2;
/* 119 */     indic2.previous = indic1;
/*     */ 
/* 121 */     this.scheduleKeys[se1.day][se1.vehicle].updateTime(timeDiff);
/*     */   }
/*     */ 
/*     */   public void addToDenied(ScheduleEntry se) {
/* 125 */     this.denied.add(se);
/*     */   }
/*     */ 
/*     */   public ScheduleEntry takeFromDenied()
/*     */   {
/* 130 */     return (ScheduleEntry)this.denied.remove();
/*     */   }
/*     */ 
/*     */   public ScheduleEntry peekFirstDenied() {
/* 134 */     return (ScheduleEntry)this.denied.peekFirst();
/*     */   }
/*     */ 
/*     */   public boolean canInsertAt(ScheduleEntry indicator, double timeDiff)
/*     */   {
/* 139 */     if (this.scheduleKeys[indicator.day][indicator.vehicle].hasEnoughTimeFor(timeDiff)) {
/* 140 */       return true;
/*     */     }
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */   public double volScoreDiffWouldBe(ScheduleEntry indicator, int vol)
/*     */   {
/* 147 */     return indicator.trip.volScoreDiffWouldBe(vol);
/*     */   }
/*     */ 
/*     */   public boolean canRemoveAt(ScheduleEntry se, double timeDiff) {
/* 151 */     if (this.scheduleKeys[se.day][se.vehicle].hasEnoughTimeFor(timeDiff)) {
/* 152 */       return true;
/*     */     }
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */   public Trip returnRandomTrip2()
/*     */   {
/* 160 */     int random = this.r.nextInt(14);
/* 161 */     int counter = 0;
/* 162 */     Trip t = null;
/*     */ 
/* 164 */     for (int d = 1; d <= 5; d++) {
/* 165 */       for (int v = 1; v <= 2; v++) {
/* 166 */         for (int i = 0; i < this.scheduleKeys[d][v].trips.size(); i++) {
/* 167 */           if (counter == random) {
/* 168 */             t = (Trip)this.scheduleKeys[d][v].trips.get(i);
/*     */           }
/* 170 */           counter++;
/*     */         }
/*     */       }
/*     */     }
/* 174 */     return t;
/*     */   }
/*     */ 
/*     */   public LinkedList<Trip> returnTripsAtDay(int day) {
/* 178 */     LinkedList<Trip> trips = new LinkedList<Trip>();
/*     */ 
/* 180 */     int veh = this.r.nextInt(2) + 1;
/* 181 */     Schedule sch = this.scheduleKeys[day][veh];
/* 182 */     for (int index = 0; index < sch.trips.size(); index++) {
/* 183 */       trips.add((Trip)sch.trips.get(index));
/*     */     }
/*     */ 
/* 186 */     return trips;
/*     */   }
/*     */ 
/*     */   public Trip returnRandomTrip() {
/* 190 */     int day = this.r.nextInt(5) + 1;
/* 191 */     int vehicle = this.r.nextInt(2) + 1;
/* 192 */     Schedule s = this.scheduleKeys[day][vehicle];
/* 193 */     int tripNr = this.r.nextInt(s.trips.size());
/*     */ 
/* 195 */     return (Trip)s.trips.get(tripNr);
/*     */   }
/*     */ 
/*     */   public ScheduleEntry randomTrip(Trip trip, boolean last)
/*     */   {
/*     */     int key;

/* 204 */     if (last) {
/* 205 */       key = this.r.nextInt(trip.size());
/*     */     } else {
/* 207 */       if (trip.size() <= 1) {
/* 208 */         return null;
/*     */       }
/* 210 */       key = this.r.nextInt(trip.size() - 1) + 1;
/*     */     }
/*     */ 
/* 213 */     return (ScheduleEntry)trip.get(key);
/*     */   }
/*     */ 
/*     */   public ScheduleEntry randomS(boolean last) {
/* 217 */     Trip trip = returnRandomTrip();
/*     */     int key;

/* 221 */     if (last) {
/* 222 */       key = this.r.nextInt(trip.size());
/*     */     } else {
/* 224 */       if (trip.size() <= 1) {
/* 225 */         return null;
/*     */       }
/* 227 */       key = this.r.nextInt(trip.size() - 1) + 1;
/*     */     }
/*     */ 
/* 230 */     return (ScheduleEntry)trip.get(key);
/*     */   }
/*     */ 
/*     */   public ScheduleEntry randomDay(int day, boolean last)
/*     */   {
/* 235 */     int veh = this.r.nextInt(2) + 1;
/*     */ 
/* 237 */     return randomVehicle(day, veh, last);
/*     */   }
/*     */ 
/*     */   public ScheduleEntry randomVehicle(int day, int vehicle, boolean last) {
/* 241 */     Schedule s = this.scheduleKeys[day][vehicle];
/* 242 */     int index = this.r.nextInt(s.trips.size());
/* 243 */     Trip trip = (Trip)s.trips.get(index);
/*     */ 
/* 245 */     return randomTrip(trip, last);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 249 */     String s = "";
/* 250 */     this.volScores = 0.0D;
/*     */ 
/* 252 */     for (int day = 1; day <= 5; day++) {
/* 253 */       for (int vehicle = 1; vehicle <= 2; vehicle++) {
/* 254 */         Schedule sch = this.scheduleKeys[day][vehicle];
/* 255 */         s = s + "Day: " + day + " vehicle: " + vehicle + " endTime: " + sch.endTime + "\n";
/*     */ 
/* 257 */         for (Trip trip : sch.trips) {
/* 258 */           if (this.spam) s = s + "tripday: " + day + "tripvehicle: " + vehicle + "schedulesize: " + sch.trips.size() + " volume: " + trip.volume + " volscore: " + trip.volScore;
/* 259 */           this.volScores += trip.volScore;
/* 260 */           s = s + trip.toString();
/*     */         }
/*     */       }
/*     */     }
/* 264 */     if (this.spam) System.out.println("test");
/* 265 */     return s;
/*     */   }
/*     */ 
public void writeToFile(PrintWriter writer) {
	int sequence = 1;
	ScheduleEntry current;
	
	for(int day=1; day<=5; day++) {
		for(int vehicle=1; vehicle<=2; vehicle++) {
			for(Trip trip: scheduleKeys[day][vehicle].trips) {
				current = trip.first; 					//Don't print first order because it is 0
				//System.out.println("Trip: " + trip.volume);
				
				while(current.hasNext()) {
					current = current.next;
					Order order = current.order;

					//System.out.println("Day: " + day + " vehicle: " + vehicle + "\n" + order);
					writer.println(vehicle + "; " + day + "; " + sequence + "; " + order.orderNr);
					sequence++;
				}
			}
			//System.out.println(scheduleKeys[day][vehicle].endTime);
			sequence=1;
		}
	}
		
}

/*     */ }

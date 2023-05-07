package getSourceCode21052018;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SimulatedAnnealing implements Serializable {
	private static final long serialVersionUID = 1L;
	Random r;
	int endParams;
	int checkRuns;
	int checkQ;
	int maxDistance;
	double T;
	double initT;
	boolean add;
	boolean remove;
	Solution solution;
	double[][] supports;
	int[] dictionary;
	double weight_AReplace;
	double weight_Add;
	double weight_Remove;
	int weightFCode;
	double[][] d_matrix;
	double score;
	boolean spam;
	boolean freqItems;
	Counters counters;
	Counter same;
	Counter better;
	Counter worseAccepted;
	Counter worse;
	Counter noTime;
	Counter noSpace;
	Counter volRep;
	Counter stortMoment;
	MoveCounter addOrder;
	MoveCounter addOrderRandom;
	MoveCounter removeOrder;
	MoveCounter twoOpt;
	MoveCounter sameDayRandom;
	MoveCounter sameDayBest;
	MoveCounter diffDayBest;
	MoveCounter diffDayRandom;

	public SimulatedAnnealing(Solution startSolution, double[][] matrix, boolean spm, Random rr, double[][] spprts,
		double w_AR, int maxDistance) {
		this.r = rr;
		this.spam = spm;
		this.solution = startSolution;
		this.d_matrix = matrix;
		this.supports = spprts;
		this.score = this.solution.score;
		this.counters = new Counters();
		this.same = new Counter(0, "same", this.counters);
		this.better = new Counter(0, "better", this.counters);
		this.worseAccepted = new Counter(0, "worse but accepted", this.counters);
		this.worse = new Counter(0, "worse", this.counters);
		this.noTime = new Counter(0, "noTime", this.counters);
		this.noSpace = new Counter(0, "noSpace", this.counters);
		this.addOrder = new MoveCounter(0, "addOrder", this.counters);
		this.addOrderRandom = new MoveCounter(0, "addOrderRandom", this.counters);
		this.removeOrder = new MoveCounter(0, "removeOrder", this.counters);
		this.twoOpt = new MoveCounter(0, "twoOpt", this.counters);
		this.sameDayRandom = new MoveCounter(0, "sameDayRandom", this.counters);
		this.sameDayBest = new MoveCounter(0, "sameDayBest", this.counters);
		this.diffDayBest = new MoveCounter(0, "diffDayBest", this.counters);
		this.diffDayRandom = new MoveCounter(0, "diffDayRandom", this.counters);
	}

	public Solution run(long runs, String[][] params, double[][] supports, int[] dictionary) {
		//endParams = Integer.parseInt(params[3][1]);
		this.T = (int) Double.parseDouble(params[60][4]);
		int Q = (int) Double.parseDouble(params[62][4]);
		double alpha = Double.parseDouble(params[61][4]);
		this.maxDistance = (int) Double.parseDouble(params[50][4]);
		this.weight_AReplace = Double.parseDouble(params[51][4]);
		System.out.println(weight_AReplace);
		weight_Add = Double.parseDouble(params[52][4]);
		weight_Remove = Double.parseDouble(params[53][4]);
		this.supports = supports;
		this.dictionary = dictionary;
		freqItems = Boolean.parseBoolean(params[4][1]);
		System.out.println(freqItems);
		boolean print = Boolean.parseBoolean(params[5][1]);

		double[] doubleParams = returnDoubleParams(params);
		int runGlobal = 0;
		for (long runTotal = 1; runTotal <= runs; runTotal++) {
			this.checkRuns = (int)runTotal;

			for (int q = 1; q <= Q; q++) {
				runTotal++;
				checkQ = q;
				//System.out.println(q);
				if (spam) {
					System.out.println();
					System.out.println("run: " + runTotal);
					System.out.println("Denied size: " + this.solution.denied.size());
				}
				neighbour(doubleParams);
			}

			runGlobal++;
			if (print && (Math.floorMod(runGlobal, 10) == 0)) {
				System.out.println("run: " + runGlobal);
				System.out.println(Math.floorDiv((int) score, 60));
			}
			this.counters.resetCounters();
			this.T *= alpha;
		}
		updateVars();
		System.out.println("SCORE: " + Math.floorDiv((int) score, 60));
		System.out.println(Math.floorDiv((int) this.solution.score, 60));
		return this.solution;
	}

	private void updateVars() {
		this.solution.endTimes = 0.0D;

		for (int d = 1; d <= 5; d++) {
			for (int v = 1; v <= 2; v++) {
				Schedule sch = this.solution.scheduleKeys[d][v];
				for (int i = 0; i < sch.trips.size(); i++) {
					if (((Trip) sch.trips.get(i)).size() == 1) {
						this.solution.endTimes -= 1800.0D;
					}
				}

				this.solution.endTimes += sch.endTime;
			}
		}

		this.solution.score = this.score;
	}

	private double[] returnDoubleParams(String[][] stringParams) {
		double[] doubleParams = new double[5];

		int beginNeighbourParams = Integer.parseInt(stringParams[33][0])+3;
		int eindNeighbourParams = Integer.parseInt(stringParams[33][1]);
		int j = 0;

		for (int i = beginNeighbourParams; i <= eindNeighbourParams; i++) {
			doubleParams[j] = Double.parseDouble(stringParams[i][4]);
			j++;
		}

		return doubleParams;
	}

	private boolean neighbour(double[] params) {
		double randomNum = this.r.nextDouble();
		double sum = 0.0D;

		for (int i = 0; i <= 4; i++) {
			sum += params[i];
		}

		int i = 0;
		double tillNow = 0.0D;

		tillNow += params[i];
		i++;
		if (randomNum < tillNow / sum) {
			twoOptExp();
			return true;
		}
		tillNow += params[i];

		i++;
		if (randomNum < tillNow / sum) {
			tryAddOrderRandom();
			return true;
		}
		tillNow += params[i];

		i++;
		if (randomNum < tillNow / sum) {
			tryRemoveOrder();
			return true;
		}
		tillNow += params[i];

		i++;
		if (randomNum < tillNow / sum) {
			tryReplaceOrderSameDayRandom();
			return true;
		}
		tillNow += params[i];

		if (randomNum < tillNow / sum) {
			tryReplaceOrderDiffDayRandom();
			return true;
		}
		return true;
	}

	private boolean acceptance(double score_old, double score_new, double agreeabilityMove) {

		if (!freqItems) {
			agreeabilityMove = 1;
		}
		if(agreeabilityMove <0) {
			System.out.println("hoort niet");
		}
		if (score_new < score_old) {
			this.better.up();
			if (this.spam)
				System.out.println("beter");
			return true;
		}
		if (score_new == score_old) {
			this.same.up();
			return true;
		}
		this.worse.up();
		double acceptanceP;
		if (freqItems) {
			if (checkRuns == 200) {
				// System.out.println("fisfactor: " + fisfactor);
			}
			acceptanceP = Math.exp(-((score_new - score_old) / this.T));
			//System.out.println(acceptanceP);
			acceptanceP = acceptanceP*agreeabilityMove;
			//acceptanceP *= agreeabilityMove;
			//System.out.println(acceptanceP);
			if (acceptanceP != 1) {
				//System.out.println(agreeabilityMove);
				//System.out.println(acceptanceP);
			}

		} else {
			acceptanceP = Math.exp(-((score_new - score_old) / this.T));
		}

		if (this.spam) {
			System.out.println("acceptanceP: " + acceptanceP);
			System.out.println("T: " + this.T);
			System.out.println("scoredifference: " + (score_new - score_old));
		}
		if (acceptanceP > Math.random()) {
			this.worseAccepted.up();
			return true;
		}
		return false;
	}

	private boolean twoOptExp() {
		this.twoOpt.up();
		ScheduleEntry se1 = this.solution.returnRandomTrip2().first;

		while (se1.next.order.freq != 0) {
			if (twoOptLinear(se1)) {
				return true;
			}
			se1 = se1.next;
		}

		this.twoOpt.denied();
		return false;
	}

	private boolean twoOptLinear(ScheduleEntry se1) {
		ScheduleEntry indic1 = se1.next;
		if (indic1.order.freq == 0) {
			this.stortMoment.up();
			return false;
		}

		ScheduleEntry se2 = indic1.next;

		double timeDiff = 0.0D;
		double reverseDiff = 0.0D;

		while (se2.order.freq != 0) {
			ScheduleEntry indic2 = se2.next;
			reverseDiff += calculateReverseDiff(se2.previous, se2);
			timeDiff = tryTwoOpt(se1, indic1, se2, indic2);
			if ((this.solution.canRemoveAt(se1, timeDiff + reverseDiff))
					&& (this.score > this.score + reverseDiff + timeDiff)) {
				this.twoOpt.better();
				this.twoOpt.accepted();
				this.solution.applyTwoOpt(se1, indic1, se2, indic2, timeDiff + reverseDiff);
				this.score = (this.score + timeDiff + reverseDiff);
				return true;
			}
			se2 = se2.next;
		}

		return false;
	}

	private double tryTwoOpt(ScheduleEntry se1, ScheduleEntry indic1, ScheduleEntry se2, ScheduleEntry indic2) {
		int se1MID = se1.order.matrixID;
		int indic1MID = indic1.order.matrixID;
		int se2MID = se2.order.matrixID;
		int indic2MID = indic2.order.matrixID;

		double timeDiff = this.d_matrix[se1MID][se2MID] - this.d_matrix[se2MID][indic2MID]
				+ this.d_matrix[indic1MID][indic2MID] - this.d_matrix[se1MID][indic1MID];

		return timeDiff;
	}

	private void tryAddOrderRandom() {
		this.addOrderRandom.up();
		ScheduleEntry se = this.solution.takeFromDenied();
		if (this.spam) {
			System.out.println("try add Order: " + se.order);
		}

		int freq = se.order.freq;
		ScoreDiff[] scoreDiffs = new ScoreDiff[freq];

		switch (freq) {
		case 1:
			scoreDiffs[0] = new ScoreDiff(this.r.nextInt(5) + 1, se, null, 0.0D, 0.0D, 0.0D);
			break;
		case 2:
			int day = this.r.nextInt(2) + 1;
			int day2;
			if (day == 1)
				day2 = 4;
			else
				day2 = 5;
			scoreDiffs[0] = new ScoreDiff(day, se, null, 0.0D, 0.0D, 0.0D);
			scoreDiffs[1] = new ScoreDiff(day2, (ScheduleEntry) se.copies.get(1), null, 0.0D, 0.0D, 0.0D);
			break;
		case 3:
			scoreDiffs[0] = new ScoreDiff(1, se, null, 0.0D, 0.0D, 0.0D);
			scoreDiffs[1] = new ScoreDiff(3, (ScheduleEntry) se.copies.get(1), null, 0.0D, 0.0D, 0.0D);
			scoreDiffs[2] = new ScoreDiff(5, (ScheduleEntry) se.copies.get(2), null, 0.0D, 0.0D, 0.0D);
			break;
		case 4:
			int skip = this.r.nextInt(5) + 1;
			int i = 0;

			for (int d = 1; d <= 5; d++) {
				if (d != skip) {
					scoreDiffs[i] = new ScoreDiff(d, (ScheduleEntry) se.copies.get(i), null, 0.0D, 0.0D, 0.0D);
					i++;
				}
			}

		}

		if (!tryAddOrdersRandom(scoreDiffs)) {
			this.solution.addToDenied(se);
		}
	}

	private boolean tryAddOrdersRandom(ScoreDiff[] scoreDiffs) {
		double scoreDiff = 0.0D;
		double weightedAgreeabilityMove = 1; // HIER

		for (int i = 0; i < scoreDiffs.length; i++) {
			scoreDiffs[i] = tryInsertSERandomAtDay(scoreDiffs[i]);
			if (scoreDiffs[i] == null)
				return false;
			scoreDiff = scoreDiff + scoreDiffs[i].timeDiff + scoreDiffs[i].declineDiff + scoreDiffs[i].volScoreDiff;
			
		}

		double newScore = this.score + scoreDiff;

		if (this.score > newScore) {
			this.addOrderRandom.better();
		}

		if (this.score < newScore && freqItems) {
			ArrayList<Double> agres = new ArrayList<Double>();
			for (int i = 0; i < scoreDiffs.length; i++) {
				agres.add(calculateWideAgreeabilityInsert(scoreDiffs[i].se, scoreDiffs[i].indicator));
			}
			double agreeabilityMove = calculateMean(agres);
			//System.out.println(agreeabilityMove);
			weightedAgreeabilityMove = Math.pow(agreeabilityMove, weight_Add);	
			//System.out.println(weightedAgreeabilityMove);
		}
		
		if (acceptance(this.score, newScore, weightedAgreeabilityMove)) {
			this.addOrderRandom.accepted();
			addOrders(scoreDiffs);
			this.score = newScore;
			return true;
		}
		this.addOrderRandom.denied();
		return false;
	}

	private void addOrders(ScoreDiff[] scoreDiffs) {
		if (this.spam)
			System.out.println("add Orders");
		int f = scoreDiffs.length;

		for (int i = 0; i < f; i++) {
			this.solution.decline += scoreDiffs[i].declineDiff;
			this.solution.insert(scoreDiffs[i].se, scoreDiffs[i].indicator, scoreDiffs[i].se.order.volume,
					scoreDiffs[i].timeDiff);
		}
	}

	private void tryReplaceOrderSameDayRandom() {
		this.sameDayRandom.up();
		ScheduleEntry se = this.solution.randomS(false);
		if (se == null)
			return;
		ScheduleEntry indicator = this.solution.randomDay(se.day, true);
		if (indicator == null)
			return;

		if (this.spam) {
			System.out.println("Replace Order Same Day Random");
		}

		double timeDiffR = calculateTimeDiffRemove(se);
		double timeDiffI = calculateTimeDiffInsert(se, indicator);

		if ((se == indicator) || (indicator == se.next)) {
			return;
		}

		if (se.vehicle == indicator.vehicle) {
			if (solution.canInsertAt(indicator, timeDiffR + timeDiffI) == false) {
				return;
			} else if (se.trip == indicator.trip) {
				// geen volumeprobleem
			} else if (!indicator.trip.hasEnoughSpaceFor(se.order.volume)) {
				return;
			}
		} else {
			if (solution.canInsertAt(indicator, timeDiffI) == false || solution.canRemoveAt(se, timeDiffR) == false
					|| indicator.trip.hasEnoughSpaceFor(se.order.volume) == false) {
				return;
			}
		}

		double newScore = this.score + timeDiffR + timeDiffI;
		double weightedAgreeabilityMove = 1;

		if (this.score > newScore) {
			this.sameDayRandom.better();
		}
		if (this.score < newScore && freqItems) {
		
			//System.out.println("Weighted: " + weightedAgreeabilityMove);
			double agreeabilityMove = calculateWideAgreeabilityInsert(se, indicator) * calculateWideAgreeabilityRemove(se);
			//double agreeabilityMove = calculateMean(new LinkedList<Double>(Arrays.asList(calculateWideAgreeabilityInsert(se, indicator),
			//		calculateWideAgreeabilityRemove(se))));
			//System.out.println("agreeabilityMOve: " + agreeabilityMove);

			//weightedAgreeabilityMove = agreeabilityMove*weight_AReplace;
			weightedAgreeabilityMove = Math.pow(agreeabilityMove, weight_AReplace);		
			//System.out.println("WagreeabilityMOve: " + weightedAgreeabilityMove);

		}

		if (acceptance(this.score, newScore, weightedAgreeabilityMove)) {
			this.sameDayRandom.accepted();
			this.solution.remove(se, -se.order.volume, timeDiffR);
			this.solution.insert(se, indicator, se.order.volume, timeDiffI);
			this.score = newScore;
		} else {
			this.sameDayRandom.denied();
			return;
		}
	}

	private void tryReplaceOrderDiffDayRandom() {
		this.diffDayRandom.up();
		ScheduleEntry se = this.solution.randomS(false);
		if (se == null)
			return;
		int day = se.day;
		ScoreDiff[] scoreDiffs = new ScoreDiff[se.order.freq];

		switch (se.order.freq) {
		case 1:
			int newDay = day;
			while (newDay == day) {
				newDay = this.r.nextInt(5) + 1;
			}
			scoreDiffs[0] = new ScoreDiff(newDay, se, null, 0.0D, 0.0D, 0.0D);
			tryReplaceOrdersRandom(scoreDiffs);
			break;
		case 2:
			se = (ScheduleEntry) se.copies.get(0);
			int day2;
			int day1;

			if (se.day == 2) {
				day1 = 1;
				day2 = 4;
			} else {
				day1 = 2;
				day2 = 5;
			}
			scoreDiffs[0] = new ScoreDiff(day1, se, null, 0.0D, 0.0D, 0.0D);
			scoreDiffs[1] = new ScoreDiff(day2, (ScheduleEntry) se.copies.get(1), null, 0.0D, 0.0D, 0.0D);
			tryReplaceOrdersRandom(scoreDiffs);
			break;
		case 3:
			break;
		case 4:
		}
	}

	private boolean tryReplaceOrdersRandom(ScoreDiff[] scoreDiffsI) {
		double scoreDiff = 0.0D;
		ScoreDiff[] scoreDiffsR = new ScoreDiff[scoreDiffsI.length];
		double weightedAgreeabilityMove = 1;

		for (int i = 0; i < scoreDiffsI.length; i++) {
			add = true;
			scoreDiffsI[i] = tryInsertSERandomAtDay(scoreDiffsI[i]);
			add = false;
			if (scoreDiffsI[i] == null) {
				return false;
			}
			scoreDiffsR[i] = tryRemoveSE(new ScoreDiff(0, scoreDiffsI[i].se, null, 0.0D, 0.0D, 0.0D));
			if (scoreDiffsR[i] == null) {
				return false;
			}
			scoreDiff = scoreDiff + scoreDiffsI[i].timeDiff + scoreDiffsI[i].declineDiff + scoreDiffsI[i].volScoreDiff
					+ scoreDiffsR[i].timeDiff + scoreDiffsR[i].declineDiff + scoreDiffsR[i].volScoreDiff;

		}

		double newScore = this.score + scoreDiff;

		if (this.score > newScore) {
			this.diffDayRandom.better();
		}
		if (this.score < newScore && freqItems) {
			ArrayList<Double> agres = new ArrayList<Double>();
			for (int i = 0; i < scoreDiffsI.length; i++) {
				double mean = calculateWideAgreeabilityInsert(scoreDiffsI[i].se, scoreDiffsI[i].indicator) * calculateWideAgreeabilityRemove(scoreDiffsR[i].se);
				//double mean = calculateMean(new LinkedList<Double>(Arrays.asList(calculateWideAgreeabilityInsert(scoreDiffsI[i].se, scoreDiffsI[i].indicator),
				//		calculateWideAgreeabilityRemove(scoreDiffsR[i].se))));
				agres.add(mean);
			}
			
			double agreeabilityMove = calculateMean(agres);
			weightedAgreeabilityMove = Math.pow(agreeabilityMove, weight_AReplace);		
			//weightedAgreeabilityMove = agreeabilityMove *weight_AReplace;
		}

		if (acceptance(this.score, newScore, weightedAgreeabilityMove)) {
			this.diffDayRandom.accepted();
			replaceOrders(scoreDiffsI, scoreDiffsR);
			this.score = newScore;
		} else {
			this.diffDayRandom.denied();
		}
		return true;
	}

	private void replaceOrders(ScoreDiff[] scoreDiffsI, ScoreDiff[] scoreDiffsR) {
		if (this.spam)
			System.out.println("Remove Orders");
		int f = scoreDiffsI.length;

		for (int i = 0; i < f; i++) {
			this.solution.remove(scoreDiffsR[i].se, -scoreDiffsR[i].se.order.volume, scoreDiffsR[i].timeDiff);
			this.solution.insert(scoreDiffsI[i].se, scoreDiffsI[i].indicator, scoreDiffsI[i].se.order.volume,
					scoreDiffsI[i].timeDiff);
		}
	}

	private boolean tryRemoveOrder() {
		this.removeOrder.up();
		ScheduleEntry se = this.solution.randomS(false);
		if (se == null)
			return false;
		int freq = se.order.freq;
		double scoreDiff = 0.0D;
		double weightedAgreeabilityMove = 1;

		ScoreDiff[] scoreDiffs = new ScoreDiff[freq];

		for (int i = 0; i < freq; i++) {
			ScheduleEntry currSE = (ScheduleEntry) se.copies.get(i);
			remove = true;
			try {
				scoreDiffs[i] = tryRemoveSE(new ScoreDiff(0, currSE, null, 0.0D, 0.0D, 0.0D));
				remove = false;
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println(se.trip);
				System.out.println(se);
				throw e;
			}
			if (scoreDiffs[i] == null) {
				return false;
			}
			scoreDiff = scoreDiff + scoreDiffs[i].timeDiff + scoreDiffs[i].declineDiff + scoreDiffs[i].volScoreDiff;
		}

		double newScore = this.score + scoreDiff;

		if (this.score > newScore) {
			this.removeOrder.better();
		} 
		
		if (this.score < newScore && freqItems) {
			ArrayList<Double> agres = new ArrayList<Double>();
			for (int i = 0; i < scoreDiffs.length; i++) {
				agres.add(calculateWideAgreeabilityRemove(scoreDiffs[i].se));
			}
			
			double agreeabilityMove = calculateMean(agres);
			weightedAgreeabilityMove = Math.pow(agreeabilityMove, weight_Remove);	
		}

		if (acceptance(this.score, newScore, weightedAgreeabilityMove)) {
			this.removeOrder.accepted();
			removeOrders(scoreDiffs);
			this.solution.addToDenied((ScheduleEntry) se.copies.get(0));
			this.score = newScore;
		} else {
			this.removeOrder.denied();
		}
		return true;
	}

	private void removeOrders(ScoreDiff[] scoreDiffs) {
		if (this.spam)
			System.out.println("remove Orders");
		int f = scoreDiffs.length;

		for (int i = 0; i < f; i++) {
			this.solution.decline += scoreDiffs[i].declineDiff;
			this.solution.remove(scoreDiffs[i].se, -scoreDiffs[i].se.order.volume, scoreDiffs[i].timeDiff);
		}
	}

	private ScoreDiff tryRemoveSE(ScoreDiff sd) {
		ScheduleEntry se = sd.se;
		double timeDiff = calculateTimeDiffRemove(se);
		double declineP = 3.0D * se.order.duur;
		double volScoreDiff = this.solution.volScoreDiffWouldBe(se, -se.order.volume);
		// double fisfactor = calculateWideAgreeabilityRemove(se);
		double fisfactor = 1;

		// System.out.println(fisfactor);

		if (remove) {
			fisfactor = 1;
		}

		if (this.solution.canRemoveAt(se, timeDiff)) {
			sd.update(null, timeDiff, declineP, volScoreDiff, fisfactor);
			return sd;
		}
		return null;
	}

	public ScoreDiff tryInsertSERandomAtDay(ScoreDiff sd) {
		ScheduleEntry se = sd.se;
		int day = sd.day;
		ScheduleEntry indicator = this.solution.randomDay(day, true);
		if (indicator == null) {
			return null;
		}

		double declineP = -3.0D * se.order.duur;
		double timeDiff = calculateTimeDiffInsert(se, indicator);
		double volScoreDiff = this.solution.volScoreDiffWouldBe(indicator, se.order.volume);
		// double fisfactor =calculateWideAgreeabilityInsert(se, indicator);

		double fisfactor = 1;
		if (add) {
			fisfactor = 1;
		}

		if (!this.solution.canInsertAt(indicator, timeDiff)) {
			this.noTime.up();
			return null;
		}
		if (!indicator.trip.hasEnoughSpaceFor(se.order.volume)) {
			this.noSpace.up();
			return null;
		}

		sd.update(indicator, timeDiff, declineP, volScoreDiff, fisfactor);
		return sd;
	}

	private double calculateWideAgreeabilityInsert(ScheduleEntry newSE, ScheduleEntry indicator) {
		ScheduleEntry currentSE = indicator.previous;
		
		//left
		double totalWeights = 0;
		double totalSupport = 0;
		
		for (double d = 1; d <= maxDistance; d++) {

			if (currentSE.order.orderNr == 0) {
				break;
			} else {
				double support = getSupport(currentSE, newSE);
				double weightD = 1;
				
				totalSupport += (support/weightD);
				totalWeights += (1/weightD);

				currentSE = currentSE.previous;
			}
		}

		//right
		currentSE = indicator;

		for (double d = 1; d <= maxDistance; d++) {
			if (currentSE.order.orderNr == 0) {
				break;
			} else {
				double support = getSupport(currentSE, newSE);
				double weightD = 1;
				//System.out.println(support);
				
				totalSupport += (support/weightD);
				totalWeights += (1/weightD);

				currentSE = currentSE.next;
			}
		}
		//System.out.println(totalSupport);
		//System.out.println(totalWeights);
		double weighted_Dmean = totalSupport / totalWeights;
		
		//System.out.println("weightedDmean: " + weighted_Dmean);

		return weighted_Dmean;
	}


	public double getSupport(ScheduleEntry se1, ScheduleEntry se2) {
		int orderNr1 = se1.order.orderNr;
		int orderNr2 = se2.order.orderNr;

		int supportID1 = dictionary[orderNr1];
		int supportID2 = dictionary[orderNr2];

		return supports[supportID1][supportID2];
	}

	public static double calculateMean(List<Double> list) {
		double total = 0.0;

		for (Double d : list) {
			total += d;
		}

		// System.out.println(total);

		if (list.size() == 0) {
			return 1;
		} else {
			// System.out.println("is niet nul");
			// System.out.println(total/(double)list.size());
			return total / list.size();
		}
	}

	private double calculateWideAgreeabilityRemove(ScheduleEntry newSE) {
		ScheduleEntry currentSE = newSE.previous;
		double totalSupport = 0 ;
		double totalWeights = 0;
		
		// left
		for (double d = 1; d <= maxDistance; d++) {
			if (currentSE.order.orderNr == 0) {
				break;
			} else {
				double support = getSupport(currentSE, newSE);
				double weightD = 1;
//				switch(weightFCode) {
//				case 1: weightD = 1; break;
//				case 2: weightD = d; break;
//				case 3: weightD = d/3; break;
//				default: weightD=0;
//				}
//				
				totalSupport += (support/weightD);
				totalWeights += (1/weightD);
				currentSE = currentSE.previous;

			}
		}

		currentSE = newSE.next;

		for (double d = 1; d <= maxDistance; d++) {
			if (currentSE.order.orderNr == 0) {
				break;
			} else {
				double support = getSupport(currentSE, newSE);
				double weightD = 1;
				
				totalSupport += (support/weightD);
				totalWeights += (1/weightD);

				currentSE = currentSE.next;
			}
		}

		double weighted_Dmean = totalSupport / totalWeights;
		//System.out.println("weightedMean: " + weighted_Dmean);
		
		return 2 - weighted_Dmean;
	}

	private double calculateReverseDiff(ScheduleEntry se1, ScheduleEntry se2) {
		int MID1 = se1.order.matrixID;
		int MID2 = se2.order.matrixID;

		return -this.d_matrix[MID1][MID2] + this.d_matrix[MID2][MID1];
	}

	private double calculateTimeDiffRemove(ScheduleEntry se) {
		int beforeMID = se.previous.order.matrixID;
		int afterMID = se.next.order.matrixID;
		int currentMID = se.order.matrixID;

		double timeDiff = this.d_matrix[beforeMID][afterMID] - this.d_matrix[beforeMID][currentMID] - se.order.duur
				- this.d_matrix[currentMID][afterMID];

		return timeDiff;
	}

	private double calculateTimeDiffInsert(ScheduleEntry se, ScheduleEntry indicator) {
		int indicMID = indicator.order.matrixID;

		ScheduleEntry previous = indicator.previous;
		Order o = previous.order;
		int beforeMID = o.matrixID;

		int seMID = se.order.matrixID;

		double timeDiff = -this.d_matrix[beforeMID][indicMID] + this.d_matrix[beforeMID][seMID] + se.order.duur
				+ this.d_matrix[seMID][indicMID];

		return timeDiff;
	}
}
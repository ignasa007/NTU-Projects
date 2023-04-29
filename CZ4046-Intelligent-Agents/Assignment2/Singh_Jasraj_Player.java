

class Singh_Jasraj_Player extends Player {

	// initialize the number of defects to 0
    int oppDefects1 = 0, oppDefects2 = 0;

	// check if the opponent can be truste based on their proportion fo defects from history
    float isTrusted(int oppDefects, int n){
        float defectRate = (float)oppDefects/n;
		// can be trusted if ratio <= 0.05
        if (defectRate <= 0.05) {
			return 1;
		}
		// unsure if 0.05 < ratio <= 0.20
        else if (defectRate <= 0.20) {
			return 0;
		}
		// cannot be trusted if ratio > 0.2
        return -1;
    }

    int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
		
		// start by cooperating
        if (n == 0) {
			return 0;
		}

		// update number of defects by each opponent
        oppDefects1 += oppHistory1[n-1];
        oppDefects2 += oppHistory2[n-1];

		// if either of the defected in the last round, we defect
        if (oppHistory1[n-1] == 1 || oppHistory2[n-1] == 1) {
			return 1;
		}
		// else if either one of them cannot be trusted, we defect
        else if (isTrusted(oppDefects1, n) == -1 || isTrusted(oppDefects2, n) == -1) {
			return 1;
		}
		// else if unsure if one of them can be trusted, we repeat their action
		else if (n > 2) {  
			if (isTrusted(oppDefects1, n) == 0) {
				return oppHistory1[n-2];
			} 
			else if (isTrusted(oppDefects2, n) == 0) {
				return oppHistory2[n-2];
			}
        }	
		// else we cooperate
        return 0;
    }

}


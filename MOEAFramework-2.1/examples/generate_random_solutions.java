/* Copyright 2009-2012 David Hadka
 * 
 * This file is part of the MOEA Framework.
 * 
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * The MOEA Framework is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public 
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import org.moeaframework.core.NondominatedPopulation;

import org.moeaframework.core.Solution;

//import org.moeaframework.core.variable.RealVariable;

import org.moeaframework.problem.WFG.*;


/* This code is based on the Walking Fish Group implementation.
 * 
 * Copyright 2005 The Walking Fish Group (WFG).
 *
 * This material is provided "as is", with no warranty expressed or implied.
 * Any use is at your own risk. Permission to use or copy this software for
 * any purpose is hereby granted without fee, provided this notice is
 * retained on all copies. Permission to modify the code and to distribute
 * modified code is granted, provided a notice that the code was modified is
 * included with the above copyright notice.
 *
 * http://www.wfg.csse.uwa.edu.au/
 */
class generate_random_solutions {

    /**
     * Private constructor to prevent instantiation.
     */
    private generate_random_solutions() {
        super();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //configure and run this experiment
        //NondominatedPopulation result = new Executor()
        //		.withProblem("UF1")
        //		.withAlgorithm("NSGAII")
        //		.withMaxEvaluations(10000)
        //		.distributeOnAllCores()
        //		.run();

        //display the results
        //for (Solution solution : result) {
        //	System.out.println(Arrays.toString(solution.getObjectives()));
        //	System.out.println("Number of coauthors: Number of persons");
        int numberofpoints = 1000;
        int i, j;
        int k = 1;
        int l = 10;
        //int wfgwhat=4;
       //int wfgwhatar[] = {2,3,4,5,6,7,8,9};
        int wfgwhatar[] = {8};
        int dimar[] = {2};
        //int n=k+l;
        //int M=2;
        for (int what = 0; what < wfgwhatar.length; what++) {
            for (int m = 0; m < dimar.length; m++) {
                //WFG2 wfg = new WFG2(k,l,M);
                //WFG2 wfg = new WFG2(k,l,M);
                double fit[];
                Class[] paramTypes = {int.class, int.class, int.class};
                Object[] paramValues = new Object[]{k, l, dimar[m]};
                Class<?> clazz = Class.forName("org.moeaframework.problem.WFG.WFG" + wfgwhatar[what]);
                System.out.println("invoking class: " + clazz.getName());
                Constructor<?> ctor = clazz.getConstructor(paramTypes);
                Object o = ctor.newInstance(paramValues);
                WFG wfg = (WFG) o;
                Solution sol_wfg = null;
                File file = new File("wfg" + wfgwhatar[what] + "_" + dimar[m] + "D.pf");
                Writer output = null;
                String text = null;
                output = new BufferedWriter(new FileWriter(file));


                //sol_wfg = new Solution(fit);
                //double[] v = EncodingUtils.getReal(sol_wfg);
                //double[] f = Problems.WFG6(v, k, M);
                //sol_wfg.setObjectives(f);


                //Solution solution = newSolution();
                //EncodingUtils.setReal(solution,
                //	Solutions.WFG_2_thru_7_random_soln(k, l));
                //evaluate(solution);
                //return solution;

                text = "";
                NondominatedPopulation nonsol=new NondominatedPopulation();
                while(nonsol.size()<numberofpoints){
                     sol_wfg = wfg.generate();
                    nonsol.add(sol_wfg);
                }
                Iterator<Solution> iterator = nonsol.iterator();
                while(iterator.hasNext()){
                    Solution sol=iterator.next();
                    double [] d=sol.getObjectives();
                    for (int t = 0; t < d.length; t++) {
                        text = text + " " + Double.toString(d[t]);
                    }
                    text=text+"\n";
                }
                output.write(text);
                output.close();
                System.out.println("Done");
            }

        }

    }
}

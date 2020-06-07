package replicateur.combinators;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a
// basic component programming model to program with components
// distributed applications in the Java programming language.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability. 
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or 
// data to be ensured and,  more generally, to use and operate it in the 
// same conditions as regards security. 
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.

import java.util.function.BiPredicate;

import replicateur.interfaces.CombinatorI;

// -----------------------------------------------------------------------------
/**
 * The class <code>MajorityVoteCombinator</code> implements a combinator of
 * results that selects a the result that appears the most often in among
 * the ones passed each time it is called.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-02-28</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			MajorityVoteCombinator<T>
implements	CombinatorI<T>
{
	/** a comparator used to know whether two results are equals or not.	*/
	protected BiPredicate<T,T>					equals ;
	/** a class of run time exception that will be thrown if no result has
	 *  a majority among the results to be combined.						*/
	protected Class<? extends RuntimeException>	exceptionClass ;

	/**
	 * create a majority vote combinator.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	equals != null
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param equals			a comparator used to know whether two results are equals or not.
	 * @param exceptionClass	a class of run time exception that will be thrown if no result has a majority among the results to be combined.
	 */
	public				MajorityVoteCombinator(
		BiPredicate<T, T> equals,
		Class<? extends RuntimeException> exceptionClass
		)
	{
		super() ;

		assert	equals != null ;
		this.equals = equals ;
		if (exceptionClass == null) {
			this.exceptionClass = RuntimeException.class ;
		} else {
			this.exceptionClass = exceptionClass ;
		}
	}

	/**
	 * check if the given value appears before the given index in the given
	 * array.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code bound >= 0 && values != null && bound < values.length}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param value		the value to be checked.
	 * @param bound		the index before which the value must appear to return true.
	 * @param values	the array of values on which the check is made.
	 * @return			true if <code>value</code> appears before index <code>bound</code> in the array <code>values</code>.
	 */
	protected boolean	appearsBefore(T value, int bound, T[] values)
	{
		assert	bound >= 0 && values != null && bound < values.length ;
		boolean ret = false ;
		for (int i = 0 ; i < bound && !ret ; i++) {
			if (this.equals.test(value, values[i])) {
				ret = true ;
			}
		}
		return ret ;
	}

	/**
	 * the number of times <code>value</code> appears in <code>values</code>.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code values != null}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param value		value to be counted.
	 * @param values	array on which the count is made.
	 * @return			the number of times <code>value</code> appears in <code>values</code>.
	 */
	protected int		count(T value, T[] values)
	{
		assert	values != null ;
		int count = 0 ;
		for (int i = 0 ; i < values.length ; i++) {
			if (this.equals.test(value, values[i])) {
				count++ ;
			}
		}
		return count ;
	}

	/**
	 * combine the given results to return a single one.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code results.length % 2 == 1}
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.alasca.replication.interfaces.CombinatorI#combine(java.lang.Object[])
	 */
	@Override
	public T			combine(T[] results)
	{
		assert	results != null && results.length > 0 ;
		assert	results.length % 2 == 1 ;

		int majorityCount = -1 ;
		T majorityValue = null ;

		for (int i = 0 ; i < results.length ; i++) {
			if (!this.appearsBefore(results[i], i, results)) {
				int n = this.count(results[i], results) ;
				if (n > majorityCount) {
					majorityCount = n ;
					majorityValue = results[i] ;
				}
			}
		}
		if (majorityCount <= results.length/2 || majorityValue == null) {
			try {
				throw this.exceptionClass.newInstance() ;
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e) ;
			}
		}
		return majorityValue ;
	}
}
// -----------------------------------------------------------------------------

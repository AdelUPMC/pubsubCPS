package replicateur.combinators;

import replicateur.interfaces.CombinatorI;

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



// -----------------------------------------------------------------------------
/**
 * The class <code>FixedCombinator</code> implements a combinator of results
 * that selects a given result each time it is called.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		{@code choice >= 0}
 * </pre>
 * 
 * <p>Created on : 2020-03-02</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			FixedCombinator<T>
implements	CombinatorI<T>
{
	protected final int	choice ;

	/**
	 * create a fixed combinator that selects the result with the given index.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code choice >= 0}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param choice	the index of the choice.
	 */
	public FixedCombinator(int choice)
	{
		assert	choice >= 0 ;
		this.choice = choice ;
	}

	/**
	 * return the current choice of this combinator.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @return	the current choice of this combinator.
	 */
	public int			getChoice()
	{
		return this.choice ;
	}

	/**
	 * combine the given results to return a single one.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code this.getChoice() <= results.length}
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.alasca.replication.interfaces.CombinatorI#combine(java.lang.Object[])
	 */
	@Override
	public T			combine(T[] results)
	{
		assert	results != null && results.length > 0 ;
		assert	this.getChoice() <= results.length ;
		return results[this.choice] ;
	}
}
// -----------------------------------------------------------------------------

public class BinaryTreeNode<Key extends Comparable<Key>, Value extends Comparable<Value>>
{
	private Key key;
	private Value value;
	private BinaryTreeNode<Key, Value> left, right;

	/**
	 * Initializes a new BinaryTreeNode with a given 
	 * key and value
	 */
	public BinaryTreeNode(Key key, Value value)
	{
		this.key = key;
		this.value = value;
		left = right = null;
	}
	
	/**
	 * Gets the left child node
	 * @return left child node
	 */
	public BinaryTreeNode<Key, Value> getLeft()
	{
		return left;
	}

	/**
	 * Gets the right child node
	 * @return right child node
	 */
	public BinaryTreeNode<Key, Value> getRight()
	{
		return right;
	}
	
	/**
	 * Sets the left child to a new node
	 * @param left the new node
	 */
	public void setLeft(BinaryTreeNode<Key, Value> left)
	{
		this.left = left;
	}
	
	/**
	 * Sets the right child to a new node
	 * @param right the new node
	 */
	public void setRight(BinaryTreeNode<Key, Value> right)
	{
		this.right = right;
	}
	
	/**
	 * Gets the value of the node
	 * @return the value of the node
	 */
	public Value getValue()
	{
		return value;
	}
	
	/**
	 * Set the node's value to a new value
	 * @param newValue the new value to give the node
	 */
	public void setValue(Value newValue)
	{
		value = newValue;
	}
	
	/**
	 * Gets the node's key
	 * @return the node's key
	 */
	public Key getKey()
	{
		return key;
	}
	
	/**
	 * Set the node's key to a new key
	 * @param newKey the new key to give the node
	 */
	public void setKey(Key newKey)
	{
		key = newKey;
	}

	/**
	 * Checks whether the node is a leaf or not by
	 * checking whether or not it has children
	 * @return whether or not the node is a leaf
	 */
	public boolean isLeaf()
	{
		return left == null && right == null;
	}
	
	/**
	 * Prints out the node's key
	 */
	public String toString()
	{
		return key.toString();
	}
}

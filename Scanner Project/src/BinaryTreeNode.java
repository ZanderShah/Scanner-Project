public class BinaryTreeNode<Key extends Comparable<Key>, Value extends Comparable<Value>>
{
	private Key key;
	private Value value;
	private BinaryTreeNode<Key, Value> left, right;

	public BinaryTreeNode(Key key, Value value)
	{
		this.key = key;
		this.value = value;
		left = right = null;
	}
	
	public BinaryTreeNode<Key, Value> getLeft()
	{
		return left;
	}
	
	public BinaryTreeNode<Key, Value> getRight()
	{
		return right;
	}
	
	public void setLeft(BinaryTreeNode<Key, Value> left)
	{
		this.left = left;
	}
	
	public void setRight(BinaryTreeNode<Key, Value> right)
	{
		this.right = right;
	}
	
	public Value getValue()
	{
		return value;
	}
	
	public void setValue(Value newValue)
	{
		value = newValue;
	}
	
	public Key getKey()
	{
		return key;
	}
	
	public void setKey(Key newKey)
	{
		key = newKey;
	}

	public boolean isLeaf()
	{
		return left == null && right == null;
	}
	
	public String toString()
	{
		return key.toString();
	}
}

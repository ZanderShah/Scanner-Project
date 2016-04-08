public class BinaryTree<Key extends Comparable<Key>, Value extends Comparable<Value>>
{
	private BinaryTreeNode<Key, Value> root;

	public BinaryTree()
	{
		root = null;
	}

	public boolean containsKey(Key key)
	{
		return containsKey(key, root);
	}

	private boolean containsKey(Key key, BinaryTreeNode<Key, Value> node)
	{
		if (node == null)
			return false;
		if (node.getKey().equals(key))
			return true;
		if (node.getKey().compareTo(key) > 0)
			return containsKey(key, node.getRight());
		return containsKey(key, node.getLeft());
	}

	public int size()
	{
		return size(root);
	}

	public void add(Key key, Value value)
	{
		if (root == null)
			root = new BinaryTreeNode<Key, Value>(key, value);
		else
			add(key, value, root);
	}

	private void add(Key key, Value value, BinaryTreeNode<Key, Value> node)
	{
		if (node.getKey().compareTo(key) < 0)
		{
			if (node.getRight() == null)
				node.setRight(new BinaryTreeNode<Key, Value>(key, value));
			else
				add(key, value, node.getRight());
		}
		else
		{
			if (node.getLeft() == null)
				node.setLeft(new BinaryTreeNode<Key, Value>(key, value));
			else
				add(key, value, node.getLeft());
		}
	}

	private void add(BinaryTreeNode<Key, Value> insert, BinaryTreeNode<Key, Value> node)
	{
		if (node.getKey().compareTo(insert.getKey()) < 0)
		{
			if (node.getRight() == null)
				node.setRight(insert);
			else
				add(insert, node.getRight());
		}
		else
		{
			if (node.getLeft() == null)
				node.setLeft(insert);
			else
				add(insert, node.getLeft());
		}
	}

	private int size(BinaryTreeNode<Key, Value> node)
	{
		int ret = 1;
		if (node.getLeft() != null)
			ret += size(node.getLeft());
		if (node.getRight() != null)
			ret += size(node.getRight());
		return ret;
	}

	/**
	 * Returns the object associated with a given key
	 * @param item 
	 * @return
	 */
	public Value get(Key key)
	{
		if (containsKey(key))
			return getValue(key, root);
		return null;
	}

	public boolean remove(Key key)
	{
		if (containsKey(key))
		{
			root = remove(key, root);
			return true;
		}
		return false;
	}

	private BinaryTreeNode<Key, Value> remove(Key key, BinaryTreeNode<Key, Value> node)
	{
		if (node == null)
			return null;

		if (node.getKey().compareTo(key) > 0)
			node.setRight(remove(key, node.getRight()));
		else if (node.getKey().compareTo(key) < 0)
			node.setLeft(remove(key, node.getLeft()));
		else
		{
			if (node.getRight() == null && node.getLeft() == null)
				return null;
			else if (node.getLeft() == null)
				return node.getRight();
			else if (node.getRight() == null)
				return node.getLeft();
			else
			{
				add(node.getLeft(), node.getRight());
				return node.getRight();
			}
		}

		return node;
	}

	public boolean isEmpty()
	{
		return root == null;
	}
}

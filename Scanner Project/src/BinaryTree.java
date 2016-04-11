public class BinaryTree<Key extends Comparable<Key>, Value extends Comparable<Value>>
{
	private BinaryTreeNode<Key, Value> root;

	/**
	 * Initializes the tree by setting the root to null so that the user can
	 * clear the tree by putting a new one on top of it
	 */
	public BinaryTree()
	{
		root = null;
	}

	/**
	 * Checks whether or not a key is inside the tree
	 * @param key the key to look for
	 * @return whether or not the key is inside the tree
	 */
	public boolean containsKey(Key key)
	{
		return containsKey(key, root);
	}

	/**
	 * Auxiliary method for containsKey
	 * @param key the key to look for
	 * @param node the current node to look at
	 * @return whether or not the key is inside the tree
	 */
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

	/**
	 * Gets the size of the tree
	 * @return the size of the tree
	 */
	public int size()
	{
		return size(root);
	}

	/**
	 * Adds a new node to the tree
	 * @param key the key for the new node
	 * @param value the value for the new node
	 */
	public void add(Key key, Value value)
	{
		if (root == null)
			root = new BinaryTreeNode<Key, Value>(key, value);
		else
			add(new BinaryTreeNode<Key, Value>(key, value), root);
	}

	/**
	 * Auxiliary method for add
	 * @param insert the new node
	 * @param node the current node to look at
	 */
	private void add(BinaryTreeNode<Key, Value> insert,
			BinaryTreeNode<Key, Value> node)
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

	/**
	 * Auxiliary node for size
	 * @param node the current node to look at
	 * @return the size of the tree
	 */
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
	 * Retrieves the value associated with a given key
	 * @param key the given key
	 * @return the value associated with the given key
	 */
	public Value getValue(Key key)
	{
		return getValue(key, root);
	}

	/**
	 * Auxiliary method for getValue
	 * @param key the given key
	 * @param node the current node to look at
	 * @return the value associated with the given key
	 */
	public Value getValue(Key key, BinaryTreeNode<Key, Value> node)
	{
		if (node.getKey().compareTo(key) < 0)
		{
			if (node.getRight() == null)
				return null;
			else
				return getValue(key, node.getRight());
		}
		else if (node.getKey().compareTo(key) > 0)
		{
			if (node.getLeft() == null)
				return null;
			else
				return getValue(key, node.getLeft());
		}
		return node.getValue();
	}

	/**
	 * Removes the node associated with a given key
	 * @param key the given key
	 * @return whether or not the key is in the tree
	 */
	public boolean remove(Key key)
	{
		if (containsKey(key))
		{
			root = remove(key, root);
			return true;
		}
		return false;
	}

	/**
	 * Auxiliary method for remove
	 * @param key the given key
	 * @param node the current node to look at
	 * @return the new node that is the left / right child of the current node
	 */
	private BinaryTreeNode<Key, Value> remove(Key key,
			BinaryTreeNode<Key, Value> node)
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

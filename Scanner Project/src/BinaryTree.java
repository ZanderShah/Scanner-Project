public class BinaryTree<E extends Comparable<E>>
{
	private BinaryTreeNode<E> root;

	public BinaryTree()
	{
		root = null;
	}

	public boolean contains(E item)
	{
		return contains(item, root);
	}

	private boolean contains(E item, BinaryTreeNode<E> node)
	{
		if (node == null)
			return false;
		if (node.getItem().equals(item))
			return true;
		if (node.getItem().compareTo(item) > 0)
			return contains(item, node.getRight());
		return contains(item, node.getLeft());
	}

	public int size()
	{
		return size(root);
	}

	public void add(E item)
	{
		if (root == null)
			root = new BinaryTreeNode<E>(item);
		else
			add(item, root);
	}

	private void add(E item, BinaryTreeNode<E> node)
	{
		if (node.getItem().compareTo(item) < 0)
		{
			if (node.getRight() == null)
				node.setRight(new BinaryTreeNode<E>(item));
			else
				add(item, node.getRight());
		}
		else
		{
			if (node.getLeft() == null)
				node.setLeft(new BinaryTreeNode<E>(item));
			else
				add(item, node.getLeft());
		}
	}

	private void add(BinaryTreeNode<E> insert, BinaryTreeNode<E> node)
	{
		if (node.getItem().compareTo(insert.getItem()) < 0)
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

	private int size(BinaryTreeNode<E> node)
	{
		int ret = 1;
		if (node.getLeft() != null)
			ret += size(node.getLeft());
		if (node.getRight() != null)
			ret += size(node.getRight());
		return ret;
	}

	public E get(E item)
	{
		if (contains(item))
			return item;
		return null;
	}

	public boolean remove(E item)
	{
		if (contains(item))
		{
			root = remove(item, root);
			return true;
		}
		return false;
	}

	private BinaryTreeNode<E> remove(E item, BinaryTreeNode<E> node)
	{
		if (node == null)
			return null;

		if (node.getItem().compareTo(item) > 0)
			node.setRight(remove(item, node.getRight()));
		else if (node.getItem().compareTo(item) < 0)
			node.setLeft(remove(item, node.getLeft()));
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

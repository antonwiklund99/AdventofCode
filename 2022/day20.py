with open("data/data20", "r") as f:
  lines = [x.strip() for x in f.readlines()]

class Node:
  def __init__(self, prev, next, value):
    self.prev = prev
    self.next = next
    self.value = value

def create_list():
  head = None
  tail = None
  move_order = []
  zero_node = None
  for line in lines:
    if head == None:
      head = Node(None, None, int(line))
      tail = head
      head.next = head
    else:
      new_node = Node(tail, head, int(line))
      tail.next = new_node
      tail = new_node
    head.prev = tail
    move_order.append(tail)
    if tail.value == 0:
      zero_node = tail
  return (move_order,zero_node)

# Part 1
(move_order,zero_node) = create_list()
for node in move_order:
  if node.value == 0:
    continue
  add = node
  for i in range(node.value % (len(move_order)-1)):
    add = add.next
  # Remove node
  node.next.prev = node.prev
  node.prev.next = node.next
  # Add node
  node.next = add.next
  node.next.prev = node
  node.prev = add
  add.next = node
node = zero_node
res = 0
for i in range(3001):
  if i != 0 and i % 1000 == 0:
    res += node.value
  node = node.next
print(res)

# Part 2
(move_order,zero_node) = create_list()
for node in move_order:
  node.value = node.value*811589153
for m in range(10):
  for node in move_order:
    if node.value == 0:
      continue
    add = node
    for i in range(node.value % (len(move_order)-1)):
      add = add.next
    # Remove node
    node.next.prev = node.prev
    node.prev.next = node.next
    # Add node
    node.next = add.next
    node.next.prev = node
    node.prev = add
    add.next = node
node = zero_node
res = 0
for i in range(3001):
  if i != 0 and i % 1000 == 0:
    res += node.value
  node = node.next
print(res)
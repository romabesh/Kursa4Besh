package sample.classes;

import sample.Configuration;

import java.util.ArrayList;

public class MemoryScheduler
{
    private final int availableMemory;
    private ArrayList<MemoryBlock> memoryBlocks;

    public MemoryScheduler(int availableMemory) {
        this.availableMemory = availableMemory;
        memoryBlocks = new ArrayList<>();
    }

    /**
     * Returns the start of free block with size equal or greater than given size.
     */
    private int findFreeBlockStart(int size) {
        if(size > availableMemory) return -1;

        if(memoryBlocks.isEmpty()) {
            return 0;
        }

        memoryBlocks.sort(MemoryBlock.byEnd);

        MemoryBlock smallestBlock = new MemoryBlock(0 , availableMemory - 1);
        boolean bool = false;

        for (int i = 0; i < memoryBlocks.size(); i++)
        {
            int blockStart;
            int emptySpaceSize;
            if(i + 1 >= memoryBlocks.size()) emptySpaceSize = availableMemory;
            else emptySpaceSize = memoryBlocks.get(i+1).start;
            emptySpaceSize -= memoryBlocks.get(i).end;
            blockStart = memoryBlocks.get(i).end + 1;
            if(emptySpaceSize > size){
                if(smallestBlock.end - smallestBlock.start > emptySpaceSize){
                    smallestBlock = new MemoryBlock(blockStart , blockStart + emptySpaceSize - 1);
                    bool = true;
                }
            }
        }
        if(bool){
            return smallestBlock.start;
        }else
            return -1;
    }

    public MemoryBlock fillMemoryBlock(int size) {
        if(size > availableMemory){
            return null;
        }
        int start = findFreeBlockStart(size);
        if(start == -1){
            return null;
        }
        MemoryBlock result = new MemoryBlock(start, start + size - 1);
        if(addBlock(result)){
            return result;
        }
        return null;
    }

    public void releaseMemoryBlock(MemoryBlock block) {
        if(block == null){
            return;
        }
        memoryBlocks.remove(block);
    }

    public boolean addBlock(MemoryBlock block) {
        if(block == null){
            return false;
        }
        return memoryBlocks.add(block);
    }

    public int getMemoryUsage() {
        int busy = 0;
        for (MemoryBlock block : memoryBlocks) {
            busy += block.end - block.start;
        }
        return busy;
    }

    public int getAvailableMemory() {
        return availableMemory;
    }
}

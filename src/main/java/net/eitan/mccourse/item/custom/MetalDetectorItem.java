package net.eitan.mccourse.item.custom;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MetalDetectorItem extends Item {
    public MetalDetectorItem(Settings settings) {
        super(settings);
    }

    // this is an event when you right click on a block
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        // checks if world is on a server or from client
        if (context.getWorld().isClient()) {
            // context gets data, mostly seen from f3
            BlockPos positionClicked = context.getBlockPos();
            PlayerEntity player = context.getPlayer();
            boolean foundBlock = false;

            // gets every block from the y + 64, since y min is -64, gets every block below
            for (int i = 0; i <= positionClicked.getY()+64; i++) {
                BlockState blockState = context.getWorld().getBlockState(positionClicked.down(i));
                Block block = blockState.getBlock();

                if (isValueableBlock(blockState)) {
                    outputValueableCoordinates(positionClicked.down(i), player, block);
                    foundBlock = true;

                    break;
                }
            }
            if (!foundBlock) {
                player.sendMessage(Text.translatable("item.mc-course.metal_detector.no_valueables"));
            }
        }

        context.getStack().damage(1, context.getPlayer(), playerEntity -> playerEntity.sendToolBreakStatus(playerEntity.getActiveHand()));

        return ActionResult.SUCCESS;
    }

    private void outputValueableCoordinates(BlockPos position, PlayerEntity player, Block block) {
        player.sendMessage(Text.literal("Valuable Found! " + block.getName().getString() + " at (" + position.getX() + ", " + position.getY() + ", " + position.getZ() + ")"));
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.mc-course.metal_detector.tooltip.shift"));
        } else {
            tooltip.add(Text.translatable("tooltip.mc-course.metal_detector.tooltip"));
        }
    }

    public boolean isValueableBlock(BlockState blockState) {
        return blockState.getBlock() == Blocks.IRON_ORE 
        || blockState.getBlock() == Blocks.DIAMOND_ORE
        || blockState.getBlock() == Blocks.GOLD_ORE
        || blockState.getBlock() == Blocks.REDSTONE_ORE
        || blockState.getBlock() == Blocks.DEEPSLATE_DIAMOND_ORE
        || blockState.getBlock() == Blocks.DEEPSLATE_GOLD_ORE
        || blockState.getBlock() == Blocks.DEEPSLATE_REDSTONE_ORE
        || blockState.getBlock() == Blocks.DEEPSLATE_IRON_ORE;
    }
}
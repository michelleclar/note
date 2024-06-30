import type { Meta, StoryObj } from "@storybook/react";
import { DialogDemo } from "./draft";

const meta = {
    title: "UI/draft",
    component: DialogDemo,
    parameters: {
        layout: "fullscreen",
    },
    tags: ["autodocs"],
} satisfies Meta<typeof DialogDemo>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};
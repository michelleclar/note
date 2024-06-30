import type { Meta, StoryObj } from "@storybook/react";
import { LogInButton } from "./card";

const meta = {
    title: "UI/Card",
    component: LogInButton,
    parameters: {
        layout: "centered",
    },
    tags: ["autodocs"],
} satisfies Meta<typeof LogInButton>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};
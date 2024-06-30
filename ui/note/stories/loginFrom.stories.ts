import type { Meta, StoryObj } from "@storybook/react";

import { LoginForm } from "./loginFrom";

const meta = {
    title: "UI/LoginFrom",
    component: LoginForm,
    parameters: {
        layout: "centered",
    },
    tags: ["autodocs"],
} satisfies Meta<typeof LoginForm>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};
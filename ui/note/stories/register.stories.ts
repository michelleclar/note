import type { Meta, StoryObj } from "@storybook/react";

import { RegisterForm } from "./register";

const meta = {
    title: "UI/RegisterFrom",
    component: RegisterForm,
    parameters: {
        layout: "centered",
    },
    tags: ["autodocs"],
} satisfies Meta<typeof RegisterForm>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};
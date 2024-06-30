import type { Meta, StoryObj } from "@storybook/react";
import { fn } from "@storybook/test";
import { ButtonModel } from "./button";

const meta = {
    title: "UI/Button",
    component: ButtonModel,
    parameters: {
        layout: "centered",
    },
    tags: ["autodocs"],
    argTypes: {
        variant: {
            control: "select",
            options: ["default", "destructive", "outline", "secondary", "ghost", "link", "purple"],
        },
        size: {
            control: "radio",
            options: ["default", "sm", "lg", "icon"],
        },
    },
    args: { onClick: fn() },
} satisfies Meta<typeof ButtonModel>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        variant: "default",
        size: "default",
        label: "Button",
    },
};

export const GithubLogin: Story = {
    args: {
        variant: "outline",
        size: "default",
        label: "Continue with GitHub",
    },
};

export const Large: Story = {
    args: {
        variant: "purple",
        size: "lg",
        label: "Button",
    },
};

export const Small: Story = {
    args: {
        variant: "ghost",
        size: "sm",
        label: "Button",
    },
};
export const Icon: Story = {
    args: {
        variant: "outline",
        size: "icon",
        label: "Button",
    },
};